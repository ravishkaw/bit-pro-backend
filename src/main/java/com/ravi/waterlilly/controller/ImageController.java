package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// image related api request controller
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    // get image
    @GetMapping("/{category}/{imageName}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String category,
            @PathVariable String imageName) throws IOException {
        Resource resource = imageService.loadImage(category, imageName);

        // Determine the MediaType based on the file extension
        String contentType = determineContentType(imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // upload  image
    @PostMapping("/{category}")
    public ResponseEntity<String> uploadImage(
            @PathVariable String category,
            @RequestParam(name = "image") MultipartFile image) throws IOException {
        String imagePath = imageService.uploadImage(category, image);
        return new ResponseEntity<>(imagePath, HttpStatus.CREATED);
    }

    // delete image
    @DeleteMapping("/{category}/{imageName}")
    public ResponseEntity<Boolean> deleteImage(
            @PathVariable String category,
            @PathVariable String imageName) throws IOException {
        Boolean status = imageService.deleteImage(category, imageName);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    // Helper method to determine the content type based on the file extension
    private String determineContentType(String imageName) {
        if (imageName.endsWith(".jpg") || imageName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (imageName.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else {
            // Default to octet-stream if the file type is unknown
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}