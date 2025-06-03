package com.ravi.waterlilly.service;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface PDFService {
    // Load pdf
    Resource loadPDF(String category, String fileName) throws IOException;
}
