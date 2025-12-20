Demand forecast 
package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DemandForecast;
import com.example.demo.repository.DemandForecastRepository;
import com.example.demo.service.DemandForecastService;

@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    private final DemandForecastRepository demandForecastRepository;

    public DemandForecastServiceImpl(DemandForecastRepository demandForecastRepository) {
        this.demandForecastRepository = demandForecastRepository;
    }

    @Override
    public DemandForecast saveForecast(DemandForecast forecast) {
        return demandForecastRepository.save(forecast);
    }

    @Override
    public List<DemandForecast> getAllForecasts() {
        return demandForecastRepository.findAll();
    }
}