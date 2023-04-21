package com.azmitia.inventoryhub100.controller;

import com.azmitia.inventoryhub100.dto.SummaryDTO;
import com.azmitia.inventoryhub100.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping("/{userId}")
    public ResponseEntity<SummaryDTO> getSummaryByUserId(@PathVariable String userId) {
        SummaryDTO summaryDTO = summaryService.getSummaryByUserId(userId);
        return new ResponseEntity<>(summaryDTO, HttpStatus.OK);
    }
}
