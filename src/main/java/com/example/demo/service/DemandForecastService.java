package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.DemandForecast;

public interface DemandForecastService {

    DemandForecast saveForecast(DemandForecast demandForecast);

    List<DemandForecast> getAllForecasts();

    DemandForecast getForecastById(Long id);
}