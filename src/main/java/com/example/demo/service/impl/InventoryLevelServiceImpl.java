package com.example.demo.service.impl;

import com.example.demo.entity.InventoryLevel;
import com.example.demo.service.InventoryLevelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryLevelServiceImpl implements InventoryLevelService {

    @Override
    public InventoryLevel createOrUpdateInventory(InventoryLevel inventory) {
        return inventory;
    }

    @Override
    public List<InventoryLevel> getInventoryForStore(Long storeId) {
        return List.of();
    }

    @Override
    public List<InventoryLevel> getInventoryForProduct(Long productId) {
        return List.of();
    }
}
