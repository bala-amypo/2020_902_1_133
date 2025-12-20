package com.example.demo.controller;

import com.example.demo.entity.InventoryLevel;
import com.example.demo.service.InventoryLevelService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryLevelController {

    private final InventoryLevelService inventoryLevelService;

    public InventoryLevelController(InventoryLevelService inventoryLevelService) {
        this.inventoryLevelService = inventoryLevelService;
    }

    @PostMapping
    public ResponseEntity<InventoryLevel> createOrUpdateInventory(
            @RequestBody InventoryLevel inventoryLevel) {

        InventoryLevel saved =
                inventoryLevelService.createOrUpdateInventory(inventoryLevel);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<InventoryLevel>> getInventoryForStore(
            @PathVariable Long storeId) {

        return ResponseEntity.ok(
                inventoryLevelService.getInventoryForStore(storeId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryLevel>> getInventoryForProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryLevelService.getInventoryForProduct(productId));
    }
}