package com.example.demo.service.impl;

import com.example.demo.entity.DemandForecast;
import com.example.demo.service.DemandForecastService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    @Override
    public DemandForecast createForecast(DemandForecast forecast) {
        return forecast;
    }

    @Override
    public List<DemandForecast> getForecastsForStore(Long storeId) {
        return List.of();
    }

    @Override
    public DemandForecast getForecast(Long storeId, Long productId) {
        return null;
    }
}
