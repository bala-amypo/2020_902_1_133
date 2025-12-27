package com.example.demo.controller;

import com.example.demo.entity.TransferSuggestion;
import com.example.demo.service.InventoryBalancerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class TransferSuggestionController {
    
    private final InventoryBalancerService inventoryBalancerService;
    
    public TransferSuggestionController(InventoryBalancerService inventoryBalancerService) {
        this.inventoryBalancerService = inventoryBalancerService;
    }
    
    @PostMapping("/generate/{productId}")
    public ResponseEntity<List<TransferSuggestion>> generateSuggestions(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryBalancerService.generateSuggestions(productId));
    }
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<TransferSuggestion>> getSuggestionsForStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(inventoryBalancerService.getSuggestionsForStore(storeId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransferSuggestion> getSuggestionById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryBalancerService.getSuggestionById(id));
    }
}