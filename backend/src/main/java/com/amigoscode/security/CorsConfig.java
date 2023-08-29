package com.amigoscode.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer
{
    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    @Value("#{'${cors.allowed-methods}'.split(',')}")
    private List<String> allowedMethods;
    
    @Value("#{'${cors.allowed-headers}'.split(',')}")
    private List<String> allowedHeaders;
    
    @Value("#{'${cors.exposed-headers}'.split(',')}")
    private List<String> expectedHeaders;
	
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
		
		
		allowedHeaders.forEach(header ->
		{
			corsRegistration.allowedHeaders(header);
		});
		
		expectedHeaders.forEach(header ->
		{
			corsRegistration.exposedHeaders(header);
		});
		
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(allowedOrigins);
		configuration.setAllowedMethods(allowedMethods);
		configuration.setAllowedHeaders(allowedHeaders);
		configuration.setExposedHeaders(expectedHeaders);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("api/**", configuration);
		return source;
	}
	
}
