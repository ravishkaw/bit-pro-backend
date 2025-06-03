package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.service.PDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/pdf")
public class PDFController {

    private final PDFService pdfService;

    @GetMapping("/{category}/{fileName}")
    public ResponseEntity<Resource> getReceipt(@PathVariable String category, @PathVariable String fileName) throws IOException {
        Resource resource = pdfService.loadPDF(category, fileName);

        String contentType = determineContentType(resource);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private String determineContentType(Resource resource) {
        try {
            Path path = resource.getFile().toPath();
            String type = Files.probeContentType(path);
            return type != null ? type : "application/pdf";
        } catch (IOException ex) {
            return "application/pdf";
        }
    }
}
