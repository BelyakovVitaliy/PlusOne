package ru.project.plusone.config.property;

public interface VkConfig {

	int getVkBotId();

	String getVkBotAccessToken();

	int getVkApplicationId();

	String getVkApplicationClientSecret();

	double getVkApiVersion();

	String getOAuth2RedirectUrl();
}
