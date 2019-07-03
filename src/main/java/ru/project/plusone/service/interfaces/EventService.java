package ru.project.plusone.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.project.plusone.model.Event;
import ru.project.plusone.model.KudaGoEvent;
import ru.project.plusone.service.Impl.EventServiceImpl.SearchResult;

public interface EventService extends GeneralService<Event> {
	void addPicture(MultipartFile file, Event event);
	SearchResult getSearchResultFromKudaGo(KudaGoEvent conditions);
	SearchResult getMore(String link);
}
