package ru.project.plusone.config.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:vk.properties")
public class VkConfigImpl implements VkConfig {

	@Value("${vkBotId}")
	private int vkBotId;
	private String vkBotAccessToken;
	private int vkApplicationId;
	private String vkApplicationClientSecret;
	private double vkApiVersion;
	private String OAuth2RedirectUrl;

	@Autowired
	public VkConfigImpl(Environment environment) {
		try {
			vkBotId = Integer.parseInt(environment.getRequiredProperty("vkBotId"));
			vkApplicationId = Integer.parseInt(environment.getRequiredProperty("vkApplicationId"));
			vkBotAccessToken = environment.getRequiredProperty("vkBotAccessToken");
			vkApplicationClientSecret = environment.getRequiredProperty("vkApplicationClientSecret");
			vkApiVersion = Double.parseDouble(environment.getRequiredProperty("vkApiVersion"));
			OAuth2RedirectUrl = environment.getRequiredProperty("OAuth2RedirectUrl");
			if (vkBotAccessToken.isEmpty() || vkApplicationClientSecret.isEmpty() || OAuth2RedirectUrl.isEmpty()) {
				throw new NullPointerException("Check vk.properties");
			}
		} catch (Exception e) {
			System.exit(-1);
		}
	}

	@Override
	public String getOAuth2RedirectUrl() {
		return OAuth2RedirectUrl;
	}

	@Override
	public int getVkBotId() {
		return vkBotId;
	}

	@Override
	public String getVkBotAccessToken() {
		return vkBotAccessToken;
	}

	@Override
	public int getVkApplicationId() {
		return vkApplicationId;
	}

	@Override
	public String getVkApplicationClientSecret() {
		return vkApplicationClientSecret;
	}

	@Override
	public double getVkApiVersion() {
		return vkApiVersion;
	}
}
