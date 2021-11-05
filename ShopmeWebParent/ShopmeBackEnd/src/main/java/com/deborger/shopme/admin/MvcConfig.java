package com.deborger.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        exposeDirectory(registry,"user-photos");
        exposeDirectory(registry,"category-images");
        exposeDirectory(registry,"brand-images");

    }

    private void exposeDirectory(ResourceHandlerRegistry registry, String dirName) {

        Path path = Paths.get(dirName);
        String absolutePath = path.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + absolutePath + "/");
    }
}
