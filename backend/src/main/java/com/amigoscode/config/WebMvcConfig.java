package com.amigoscode.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    @Value("#{'${cors.allowed-methods}'.split(',')}")
    private List<String> allowedMethods;
	
	@Value("#{'$cors.allowed-headers}'.split(',')}")
	private List<String> allowedHeaders;
	
	
	@Override
	public void addCorsMappings(CorsRegistry registry)
	{
		CorsRegistration corsRegistration = registry.addMapping("/api/**");
		
		allowedOrigins.forEach(origin ->
		{
			corsRegistration.allowedOrigins(origin);
		});
		
		allowedMethods.forEach(method ->
		{
			corsRegistration.allowedMethods(method);
		});
		
	}
}
