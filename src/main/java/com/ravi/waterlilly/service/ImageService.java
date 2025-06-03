package com.ravi.waterlilly.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Resource loadImage(String category, String fileName) throws IOException;

    String uploadImage(String category, MultipartFile file) throws IOException;

    boolean deleteImage(String category, String fileName) throws IOException;
}
