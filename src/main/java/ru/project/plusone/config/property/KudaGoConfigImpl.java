package ru.project.plusone.config.property;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:kudago.properties")
public class KudaGoConfigImpl implements KudaGoConfig {
	private String baseApiURL;
	private String version;
	private String type;
	private String language;
	private String cType;
	private String expand;
	private String cTypeGettingDetails;
	private String fieldsGettingDetails;
	private String pageSize;


	public KudaGoConfigImpl(Environment environment) {
		try {
			baseApiURL = environment.getRequiredProperty("baseApiURL");
			version = environment.getRequiredProperty("version");
			type = environment.getRequiredProperty("type");
			language = environment.getRequiredProperty("language");
			cType = environment.getRequiredProperty("ctype");
			expand = environment.getRequiredProperty("expand");
			cTypeGettingDetails = environment.getRequiredProperty("ctypeGettingDetails");
			fieldsGettingDetails = environment.getRequiredProperty("fieldsGettingDetails");
			pageSize = environment.getRequiredProperty("page_size");
			if (baseApiURL.isEmpty() || version.isEmpty() || type.isEmpty() || language.isEmpty() || expand.isEmpty()
					|| cType.isEmpty() || cTypeGettingDetails.isEmpty() || fieldsGettingDetails.isEmpty() || pageSize.isEmpty()) {
				throw new NullPointerException("Check kudago.properties");
			}
		} catch (Exception e) {
			System.exit(-1);
		}
	}

	@Override
	public String getBaseApiURL() {
		return baseApiURL;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public String getCType() {
		return cType;
	}

	@Override
	public String getExpand() {
		return expand;
	}

	@Override
	public String getCTypeGettingDetails() {
		return cTypeGettingDetails;
	}

	@Override
	public String getFieldsGettingDetails() {
		return fieldsGettingDetails;
	}

	@Override
	public String getPageSize() {
		return pageSize;
	}
}
