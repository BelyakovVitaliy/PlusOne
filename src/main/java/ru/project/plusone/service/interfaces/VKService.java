package ru.project.plusone.service.interfaces;

import com.vk.api.sdk.exceptions.ClientException;
import ru.project.plusone.model.Event;
import ru.project.plusone.model.User;

public interface VKService extends OAuthSocialsService{
	Integer sendMessage(User user, String msg) throws ClientException;

	String joinToConf(Event event);

	boolean isUserFriend(User user);

	Integer createChat(User user);

}

