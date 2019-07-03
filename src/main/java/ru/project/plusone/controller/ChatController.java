package ru.project.plusone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import ru.project.plusone.model.ChatMessage;
import ru.project.plusone.model.User;
import ru.project.plusone.service.interfaces.UserService;

@Controller
public class ChatController {

	@Autowired
	private final UserService userService;

	public ChatController(UserService userService) {
		this.userService = userService;
	}

	@MessageMapping("/event/sendMessage/{id}")
	@SendTo("/topic/event/chat/{id}")
	public ChatMessage receiveMessage(String message, SimpMessageHeaderAccessor sha) {
		User user = userService.getLocalUserByEmail(sha.getUser().getName());
		return new ChatMessage(userService.getLocalUserByEmail(user.getEmail()), message);
	}
}
