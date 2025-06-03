package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${project.image}")
    String basePath;

    // Load image
    @Override
    public Resource loadImage(String category, String fileName) throws IOException {
        Path filePath = Paths.get(basePath, category).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Could not read file: " + fileName);
        }
    }

    // Upload an image
    @Override
    public String uploadImage(String category, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String fileName = null;

        if (originalFileName != null) {
            fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        }

        String categoryPath = basePath + File.separator + category;
        String filePath = categoryPath + File.separator + fileName;

        // Create category folder if it doesn't exist
        File folder = new File(categoryPath);
        if (!folder.exists())
            folder.mkdirs();

        Files.copy(file.getInputStream(), Paths.get(filePath));
        return category + "/" + fileName;
    }

    // Delete an image
    @Override
    public boolean deleteImage(String category, String fileName) throws IOException {
        // Check if fileName already contains the category path
        Path filePath;
        if (fileName.startsWith(category + "/")) {
            // Use fileName as is if it already contains the category
            filePath = Paths.get(basePath).resolve(fileName).normalize();
        } else {
            // Otherwise append category to path
            filePath = Paths.get(basePath, category).resolve(fileName).normalize();
        }

        File file = filePath.toFile();

        if (file.exists()) {
            return file.delete();
        } else {
            throw new IOException("Could not find file to delete: " + fileName);
        }
    }
}
