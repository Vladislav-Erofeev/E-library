package ru.library.ELibrary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    private final String UPLOAD_DIRECTORY = System.getProperty("user.dir") +  "/src/main/resources/static/images/books/"; // на локалке
//    private final String UPLOAD_DIRECTORY = System.getProperty("user.dir") +  "/"; // в docker

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + UPLOAD_DIRECTORY);
    }


}
