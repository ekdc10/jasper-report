package com.eka.user.controller;

import com.eka.user.service.UserReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that provides an endpoint to download user reports as PDF.
 */
@RestController
@Tag(name = "User Report API", description = "API to download user reports")
public class UserReportController {

    @Autowired
    private UserReportService userReportService;

    /**
     * GET /api/report/user
     * Generates and returns a PDF report of all users.
     *
     * @return ResponseEntity containing the PDF bytes and appropriate HTTP headers.
     */
    @GetMapping("/api/report/user")
    public ResponseEntity<byte[]> downloadUserReport() {
        byte[] pdf = userReportService.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("users.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
