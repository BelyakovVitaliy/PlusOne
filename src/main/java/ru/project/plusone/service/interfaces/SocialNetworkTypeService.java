package ru.project.plusone.service.interfaces;

import ru.project.plusone.model.SocialNetworkType;

public interface SocialNetworkTypeService extends GeneralService<SocialNetworkType> {
	SocialNetworkType getByName(String name);
}
