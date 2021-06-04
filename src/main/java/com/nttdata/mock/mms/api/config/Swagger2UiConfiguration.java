package com.nttdata.mock.mms.api.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2UiConfiguration implements WebMvcConfigurer {

	@Bean
	public Docket eDesignApi(SwaggerConfigProperties swaggerConfigProperties) {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo(swaggerConfigProperties))
				.enable(Boolean.valueOf(swaggerConfigProperties.getEnabled()))
				.select()
				.apis(RequestHandlerSelectors.basePackage(swaggerConfigProperties.getBasePackage()))
				.build()
				.securitySchemes(Collections.singletonList(new ApiKey("Bearer", swaggerConfigProperties.getApiKeyName(), "header")));
	}

	private ApiInfo apiInfo(SwaggerConfigProperties swaggerConfigProperties) {
		return new ApiInfoBuilder()
				.title(swaggerConfigProperties.getTitle())
				.description(swaggerConfigProperties.getDescription())
				.version(swaggerConfigProperties.getApiVersion())
				.build();
	}
}
