package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "demand_forecasts")
public class DemandForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;
    
    @Column(name = "forecasted_demand", nullable = false)
    private Integer forecastedDemand;
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public LocalDate getForecastDate() { return forecastDate; }
    public void setForecastDate(LocalDate forecastDate) { this.forecastDate = forecastDate; }
    
    public Integer getForecastedDemand() { return forecastedDemand; }
    public void setForecastedDemand(Integer forecastedDemand) { this.forecastedDemand = forecastedDemand; }
    
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
}