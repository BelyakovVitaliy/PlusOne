package ru.project.plusone.model;

import javax.persistence.Entity;

public class ChatMessage {
	private User sender;
	private String message;

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public ChatMessage(User sender, String message) {
		this.sender = sender;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
