package com.guitarshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableCaching
public class GuitarshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuitarshopApplication.class, args);
	}

	@Configuration
	public static class WebConfig implements WebMvcConfigurer {
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			// Map /data/** URLs to data/ folder in project root
			registry.addResourceHandler("/data/**")
					.addResourceLocations("file:data/");
		}
	}
}
