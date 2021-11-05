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
        String dirNamePhotos = "user-photos";
        Path userPhotosDir = Paths.get(dirNamePhotos);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirNamePhotos + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        String dirName = "category-images";
        Path catImagesDir = Paths.get(dirName);
        String catImagesPath = catImagesDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + catImagesPath + "/");

        String dirNameBrands = "brand-images";
        Path brandImagesDir = Paths.get(dirNameBrands);
        String brandImagesPath = brandImagesDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirNameBrands + "/**")
                .addResourceLocations("file:/" + brandImagesPath + "/");
    }
}
