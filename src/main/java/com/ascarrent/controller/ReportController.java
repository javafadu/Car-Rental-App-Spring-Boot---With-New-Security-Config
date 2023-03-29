package com.ascarrent.controller;

import com.ascarrent.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;


    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // **** USER REPORT ****
    @GetMapping("/download/users")
    @PreAuthorize("hasRole('ADMIN')")
    // Resource : org.springframework.core.io.Resource
    public ResponseEntity<Resource> getUserReport() {
        String fileName = "users.xlsx";
        ByteArrayInputStream bais = reportService.getUserReport();
        InputStreamResource file = new InputStreamResource(bais);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vmd.ms-excel"))
                .body(file);
    }


    // **** CAR REPORT ****
    @GetMapping("/download/cars")
    @PreAuthorize("hasRole('ADMIN')")
    // Resource : org.springframework.core.io.Resource
    public ResponseEntity<Resource> getCarReport() {
        String fileName = "cars.xlsx";
        ByteArrayInputStream bais = reportService.getCarReport();
        InputStreamResource file = new InputStreamResource(bais);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vmd.ms-excel"))
                .body(file);
    }

    // **** RESERVATION REPORT ****
    @GetMapping("/download/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    // Resource : org.springframework.core.io.Resource
    public ResponseEntity<Resource> getReservationReport() {
        String fileName = "reservations.xlsx";
        ByteArrayInputStream bais = reportService.getReservationReport();
        InputStreamResource file = new InputStreamResource(bais);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vmd.ms-excel"))
                .body(file);
    }



}
