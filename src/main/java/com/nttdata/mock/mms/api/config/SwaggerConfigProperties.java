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
	
}