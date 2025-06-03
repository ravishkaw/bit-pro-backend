package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.service.PDFService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PDFServiceImpl implements PDFService {

    @Value("pdf/")
    String basePath;

    @Override
    public Resource loadPDF(String category, String fileName) throws IOException {
        Path filePath = Paths.get(basePath, category).resolve(fileName).normalize();
        System.out.println(filePath.toAbsolutePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Could not read file: " + fileName);
        }
    }
}
