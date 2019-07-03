package ru.project.plusone.service.Impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.project.plusone.config.property.KudaGoConfig;
import ru.project.plusone.exception.EventException;
import ru.project.plusone.exception.EventPhotoException;
import ru.project.plusone.exception.KudaGoEventException;
import ru.project.plusone.model.*;
import ru.project.plusone.repository.interfaces.EventRepository;
import ru.project.plusone.service.interfaces.EventService;
import ru.project.plusone.service.interfaces.TagService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;

	private final KudaGoConfig kudaGoConfig;

	private final TagService tagService;

	@Autowired
	public EventServiceImpl(EventRepository eventRepository, KudaGoConfig kudaGoConfig, TagService tagService) {
		this.eventRepository = eventRepository;
		this.kudaGoConfig = kudaGoConfig;
		this.tagService = tagService;
	}

	@Override
	public Set<Event> getAll() {
		return new HashSet<>(eventRepository.findAll());
	}

	@Override
	public Event get(Long id) {
		return eventRepository.getById(id);
	}

	@Override
	public void add(Event e) {
		e.setTags(setIdForExistTagFromList(e.getTags()));
		eventRepository.saveAndFlush(e);
	}

	private void checkData(Event e) {
		if (e.getTitle().isEmpty()) {
			throw new EventException("Title field can't be empty");
		}
		if (e.getStartDate() == null) {
			throw new EventException("Start date field must be fullfiend");
		}
		if (e.getEndDate() != null && ((e.getStartDate().isAfter(e.getEndDate())) || (e.getStartDate().equals(e.getEndDate())))) {
			throw new EventException("Start date can't be early then end date1  ");
		}
	}

	@Override
	public void delete(Event e) {
		eventRepository.delete(e);
	}

	@Override
	public void update(Event e) {
		checkData(e);
		e.setTags(setIdForExistTagFromList(e.getTags()));
		eventRepository.saveAndFlush(e);
	}

	@Override
	public void deleteById(Long id) {
		eventRepository.deleteById(id);
	}

	@Override
	public SearchResult getMore(String link) {
		JSONObject json = getJsonObject(link);
		return getSearchResultFromJSON(json, null, null);
	}

	@Override
	public SearchResult getSearchResultFromKudaGo(KudaGoEvent conditions) {
		conditions.setTitle(conditions.getTitle().replace(" ", "%20"));
		LocalDateTime startDate = conditions.getStartDate();
		LocalDateTime endDate = conditions.getEndDate();
		KudaGoLocation destination = conditions.getDestination();
		StringBuilder query = new StringBuilder(kudaGoConfig.getBaseApiURL() + "/" + kudaGoConfig.getVersion() + "/" +
				kudaGoConfig.getType() + "/?lang=" + kudaGoConfig.getLanguage() + "&q=" + conditions.getTitle() + "&expand=" +
				kudaGoConfig.getExpand() + "&ctype=" + kudaGoConfig.getCType() + "&page_size=" + kudaGoConfig.getPageSize());
		if(destination!=null) {
			double x = destination.getX();
			double y = destination.getY();
			int radius = destination.getRadius();
			//Если есть заполненные и незаполненные поля места, то выбросить исключение
			if(!((x == 0.0 & y == 0.0 & radius == 0) ||
				(x != 0.0 & y != 0.0 & radius != 0))) {
				throw new KudaGoEventException("All destination fields have to be fulfilled");
			}
			if(x != 0.0 & y != 0.0 & radius != 0) {
				query.append("&lat=").append(x).append("&lon=").append(y).append("&radius=").append(radius);
			}

		}
		JSONObject json = getJsonObject(query.toString());
		/*lon=37.586223&lat=55.805127*/
		return getSearchResultFromJSON(json, startDate, endDate);
	}

	private SearchResult getSearchResultFromJSON(JSONObject json, LocalDateTime startDate, LocalDateTime endDate) {
		if (startDate == null && endDate != null) {
			throw new KudaGoEventException("Неверный диапазон дат события");
		}
		Set<Event> events = new HashSet<>();
		JSONArray jsonMessages;
		try {
			jsonMessages = json.getJSONArray("results");
		} catch (Exception e1) {
			return new SearchResult(null, "");
		}
		String nextPage = "";
		try {
			nextPage = json.getString("next");
		} catch (Exception e1) {
			System.out.println("Выданы все события по данному запросу");
		}
		for (int i = 0; i < jsonMessages.length(); i++) {
			Event event = new Event();
			JSONObject jsonObject;
			try {
				jsonObject = jsonMessages.getJSONObject(i);
				event.setId(jsonObject.getLong("id"));
				event.setTitle(jsonObject.getString("title"));
				Pattern p = Pattern.compile("</?\\w>");
				event.setDescription(p.matcher(jsonObject.getString("description")).replaceAll(""));
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			JSONObject jsonDates = null;
			LocalDateTime eventStartDate = null;
			LocalDateTime eventEndDate = null;
			try {
				jsonDates = jsonObject.getJSONObject("daterange");
			} catch (JSONException e) {
				System.out.println("Не удалось получить дату начала и окончания события");
			}
			if (jsonDates != null) {
				try {
					eventStartDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(jsonDates.getLong("start")), ZoneId.systemDefault());
				} catch (JSONException e) {
					System.out.println("Не удалось получить дату начала события");
				}
				try {
					eventEndDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(jsonDates.getLong("end")), ZoneId.systemDefault());
				} catch (JSONException e) {
					System.out.println("Не удалось получить дату окончания события");
				}
			}
			if (!(startDate == null || endDate == null)) {
				if ((eventStartDate == null || eventEndDate == null)
						|| (eventStartDate.isBefore(startDate) && eventEndDate.isBefore(startDate))
						|| (eventStartDate.isAfter(endDate) && eventEndDate.isAfter(endDate))) {
					continue;
				}
			}
			event.setStartDate(eventStartDate);
			event.setEndDate(eventEndDate);
			try {
				event.setPicture(jsonObject.getJSONObject("first_image").getJSONObject("thumbnails").getString("640x384"));
			} catch (JSONException e) {
				System.out.println("Параметр \"first_image\" отсутствует");
			}
			try {
				JSONObject eventPlace = jsonObject.getJSONObject("place");
				String eventDestination = eventPlace.getString("address");
				JSONObject coords = eventPlace.getJSONObject("coords");
				double x = coords.getDouble("lat");
				double y = coords.getDouble("lon");
				event.setDestination(new Location(eventDestination,x,y));
			} catch (JSONException e) {
				System.out.println("Параметр \"place\" отсутствует");
			}
			Set<Tag> tags = new HashSet<>();
			try {
				String eventId = jsonObject.getString("id");
				JSONObject jsonEventTags = getJsonObject(kudaGoConfig.getBaseApiURL() + "/" + kudaGoConfig.getVersion() + "/" + kudaGoConfig.getCTypeGettingDetails() + "/" + eventId + "/?lang=" + kudaGoConfig.getLanguage() + "/fields" + kudaGoConfig.getFieldsGettingDetails());
				JSONArray jsonTagArray = jsonEventTags.getJSONArray("tags");
				tags = new HashSet<>();
				for (int j = 0; j < jsonTagArray.length(); j++) {
					tags.add(new Tag("#" + jsonTagArray.getString(j)));
				}
			} catch (JSONException e) {
				System.out.println("Не удалось получить список тегов для события");
			}
			event.setTags(tags);
			events.add(event);
		}
		return new SearchResult(events, nextPage);
	}

	private JSONObject getJsonObject(String url) {
		HttpGet httpGetEvents = new HttpGet(url);
		HttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(RequestConfig.custom()
						.setCookieSpec(CookieSpecs.STANDARD).build())
				.build();
		JSONObject json = null;
		try {
			HttpResponse response = httpClient.execute(httpGetEvents);
			String result = EntityUtils.toString(response.getEntity());
			json = new JSONObject(result);
		} catch (IOException | JSONException e) {
			throw new KudaGoEventException("Не удалось получить список событий");
		}
		return json;
	}

	@Override
	public void addPicture(MultipartFile file, Event event) {
		if (!file.isEmpty()) {
			try {
				BufferedImage image = ImageIO.read(new BufferedInputStream(file.getInputStream()));
				String fileName = "event-" + event.getId() + "-photo.png";
				File outputFile = new File("C:\\plusone\\pictures\\" + fileName);
				ImageIO.write(image, "png", outputFile);
				event.setPicture("/rest/pic/" + fileName);
				update(event);
			} catch (Exception e) {
				throw new EventPhotoException("There was an error saving photo");
			}
		}
	}

	private Set<Tag> setIdForExistTagFromList(Set<Tag> tags) {
		//TODO Переделать!!!!
		Tag t;
		if (tags!=null) {
			for (Tag tag : tags) {
				if ((t = tagService.getByTitle(tag.getTitle())) != null) {
					tag.setId(t.getId());
				}else {
					tagService.add(tag);
				}
			}
		}
		return tags;
	}

	public class SearchResult {
		private Set<Event> events;
		private String nextEventsURL;

		private SearchResult(Set<Event> events, String nextEventsURL) {
			this.events = events;
			this.nextEventsURL = nextEventsURL;
		}

		public Set<Event> getEvents() {
			return events;
		}

		public void setEvents(Set<Event> events) {
			this.events = events;
		}

		public String getNextEventsURL() {
			return nextEventsURL;
		}

		public void setNextEventsURL(String nextEventsURL) {
			this.nextEventsURL = nextEventsURL;
		}
	}

}
