package ru.project.plusone.service.interfaces;

import ru.project.plusone.model.SocialNetwork;

import java.util.List;

public interface SocialNetworkService extends GeneralService<SocialNetwork> {
	String getVKLinkByUserId(Long id);

	List<String> getVKLinkFromEventId(Long eventId);
}
