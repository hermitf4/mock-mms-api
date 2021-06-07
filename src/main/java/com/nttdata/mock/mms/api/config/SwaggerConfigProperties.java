package com.nttdata.mock.mms.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration("swaggerConfigProperties")
public class SwaggerConfigProperties {

	@Value("${swagger.api.version}")
	private String apiVersion;

	@Value("${swagger.enabled}")
	private String enabled = "false";

	@Value("${swagger.title}")
	private String title;

	@Value("${swagger.description}")
	private String description;

	@Value("${swagger.basePackage}")
	private String basePackage;

	@Value("${swagger.apiKeyName}")
	private String apiKeyName;

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getApiKeyName() {
		return apiKeyName;
	}

	public void setApiKeyName(String apiKeyName) {
		this.apiKeyName = apiKeyName;
	}
	
	
}