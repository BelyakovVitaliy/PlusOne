package ru.project.plusone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.project.plusone.model.Event;
import ru.project.plusone.model.User;
import ru.project.plusone.service.interfaces.EventService;

@Controller
@RequestMapping("/event/")
public class EventController {

	private final EventService eventService;

	@Autowired
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping(value = "chat/{id}")
	public ModelAndView getChatPage(@AuthenticationPrincipal User user, @PathVariable("id") Long eventId) {
		Event event = eventService.get(eventId);
		ModelAndView modelAndView = new ModelAndView();
		if(event == null) {
			modelAndView.setStatus(HttpStatus.NOT_FOUND);
			return modelAndView;
		}
		if(event.getJoinedUsers().contains(user) == (event.getOwnerUser().equals(user))) {
			modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
			return modelAndView;
		}
		modelAndView.setViewName("eventChat");
		modelAndView.addObject("event", event);
		return modelAndView;
	}
}
