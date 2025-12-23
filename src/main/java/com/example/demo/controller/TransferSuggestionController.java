package com.example.demo.controller;

import com.example.demo.service.InventoryBalancerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferSuggestionController {

    private final InventoryBalancerService inventoryBalancerService;

    public TransferSuggestionController(InventoryBalancerService inventoryBalancerService) {
        this.inventoryBalancerService = inventoryBalancerService;
    }

    @GetMapping("/suggestions")
    public List<String> getTransferSuggestions() {
        return inventoryBalancerService.generateTransferSuggestions();
    }
}
