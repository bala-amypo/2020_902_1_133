package com.example.demo.controller;

import com.example.demo.entity.DemandForecast;
import com.example.demo.service.DemandForecastService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forecasts")
public class DemandForecastController {

    private final DemandForecastService demandForecastService;

    public DemandForecastController(DemandForecastService demandForecastService) {
        this.demandForecastService = demandForecastService;
    }

    @PostMapping
    public ResponseEntity<DemandForecast> createForecast(
            @RequestBody DemandForecast forecast) {

        DemandForecast saved =
                demandForecastService.createForecast(forecast);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<DemandForecast>> getForecastsForStore(
            @PathVariable Long storeId) {

        return ResponseEntity.ok(
                demandForecastService.getForecastsForStore(storeId));
    }

    @GetMapping("/store/{storeId}/product/{productId}")
    public ResponseEntity<DemandForecast> getForecastForStoreAndProduct(
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        DemandForecast forecast =
                demandForecastService.getForecast(storeId, productId);
        return ResponseEntity.ok(forecast);
    }
}