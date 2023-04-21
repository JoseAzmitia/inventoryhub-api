package com.azmitia.inventoryhub100.service;

import com.azmitia.inventoryhub100.dto.SummaryDTO;

public interface SummaryService {
    SummaryDTO getSummaryByUserId(String userId);
}
