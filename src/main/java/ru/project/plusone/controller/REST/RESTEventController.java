package ru.project.plusone.controller.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.plusone.exception.EventException;
import ru.project.plusone.model.Event;
import ru.project.plusone.model.KudaGoEvent;
import ru.project.plusone.model.User;
import ru.project.plusone.service.interfaces.EventService;
import ru.project.plusone.service.interfaces.TagService;
import ru.project.plusone.service.interfaces.UserService;
import ru.project.plusone.service.interfaces.VKService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/rest/event/")
public class RESTEventController {

	final
	EventService eventService;
	final
	VKService vkService;
	final
	UserService userService;
	final
	TagService tagService;

	@Autowired
	public RESTEventController(EventService eventService, VKService vkService, UserService userService, TagService tagService) {
		this.eventService = eventService;
		this.vkService = vkService;
		this.userService = userService;
		this.tagService = tagService;
	}

	@PostMapping(value = "add")
	public ResponseEntity addNewEvent(@RequestBody Event event, @AuthenticationPrincipal User user) {
		event.setOwnerUser(user);
		eventService.add(event);
		return ResponseEntity.ok().body(event.getId());
	}

	@PostMapping(value = "update")
	public ResponseEntity update(@RequestBody Event event, @AuthenticationPrincipal User user) {
		Optional<String> eventPic = Optional.ofNullable(event.getPicture());
		Optional<String> currentPic = Optional.ofNullable(eventService.get(event.getId()).getPicture());
		if (currentPic.isPresent() && !eventPic.isPresent()) {
			event.setPicture(currentPic.get());
		}
		event.setOwnerUser(user);
		eventService.update(event);
		return ResponseEntity.ok().body(event.getId());
	}

	@PostMapping(value = "delete")
	public ResponseEntity deleteEvent(@RequestParam Long eventId, @AuthenticationPrincipal User user) {
		Event eventFromDB = eventService.get(eventId);
		if (!eventFromDB.getOwnerUser().equals(user)) {
			throw new EventException("You don't have permission for this action");
		}
		eventService.deleteById(eventId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = {"addPicture"}, method = RequestMethod.POST)
	public ResponseEntity addPicture(@RequestParam("pic") MultipartFile file, @RequestParam("id") Long id) {
		Event event = eventService.get(id);
		eventService.addPicture(file, event);
		return ResponseEntity.ok().body("{\"msg\":\"Сохранено\"}");
	}

	@PostMapping(value = "changeChatAvailability")
	public ResponseEntity changeChatAvailability(@RequestParam Long eventId, @RequestParam boolean isChatEnable, @AuthenticationPrincipal User user) {
		Event event = eventService.get(eventId);
		if (!event.isChatEnabled()) {
			if (event.getChatId() == 0) {
				if (vkService.isUserFriend(user)) {
					int chatId = vkService.createChat(user);
					event.setChatId(chatId);
					event.setChatEnabled(isChatEnable);
					eventService.update(event);
					return ResponseEntity.ok().body(chatId);
				} else {
					Map<String, String> map = new HashMap<>();
					map.put("message", "Необходимо добавить бота в друзья");
					map.put("link", "https://vk.com/id26833748");
					return ResponseEntity.badRequest().body(map);
				}
			}
		}
		event.setChatEnabled(isChatEnable);
		eventService.update(event);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "disableChat")
	public ResponseEntity isChatEnabled(@RequestParam Long id) {
		Event event = eventService.get(id);
		event.setChatEnabled(false);
		eventService.update(event);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "joinToEvent")
	public ResponseEntity joinToEvent(@RequestParam Long eventId, @AuthenticationPrincipal User user) {
		Event event = eventService.get(eventId);
		event.getJoinedUsers().add(user);
		eventService.update(event);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "exitFromEvent")
	public ResponseEntity exitFromEvent(@RequestParam Long eventId, @AuthenticationPrincipal User user) {
		Event event = eventService.get(eventId);
		event.getJoinedUsers().remove(user);
		eventService.update(event);
		return ResponseEntity.ok().build();
	}

	//TODO regexp на ссылку
	@PostMapping(value = "joinToVKChat")
	public ResponseEntity joinToChat(@AuthenticationPrincipal User user, @RequestParam Long eventId) {
		Event event = eventService.get(eventId);
		if (event == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		if (!event.getJoinedUsers().contains(user)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		String link = vkService.joinToConf(eventService.get(eventId));
		if (link.isEmpty()) {
			return ResponseEntity.badRequest().body("Произошла ошибка");
		}
		return ResponseEntity.ok().body(link);
	}

	@PostMapping(value = "getMoreFromKudaGo")
	public ResponseEntity getMoreFromKudaGo(@RequestParam String link) {
		return ResponseEntity.ok(eventService.getMore(link));
	}

	@PostMapping(value = "searchInKudaGo")
	public ResponseEntity searchEventInKudaGo(@RequestBody KudaGoEvent event) {
		return ResponseEntity.ok(eventService.getSearchResultFromKudaGo(event));
	}

	@GetMapping("getJoinedUsers/{id}")
	public ResponseEntity getJoinedUsers(@PathVariable Long id) {
		return ResponseEntity.ok(eventService.get(id).getJoinedUsers());
	}
}
