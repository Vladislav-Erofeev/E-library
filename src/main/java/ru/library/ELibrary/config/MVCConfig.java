package ru.library.ELibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("application.properties")
public class MVCConfig implements WebMvcConfigurer {
    private final String UPLOAD_DIRECTORY;

    @Autowired
    public MVCConfig(@Value("${upload.directory}") String upload_directory) {
        UPLOAD_DIRECTORY = upload_directory;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**", "/style/**").addResourceLocations("file:" + UPLOAD_DIRECTORY);
    }

}
