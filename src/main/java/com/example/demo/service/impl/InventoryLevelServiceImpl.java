Inventoryimpl
package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.InventoryLevel;
import com.example.demo.repository.InventoryLevelRepository;
import com.example.demo.service.InventoryLevelService;

@Service
public class InventoryLevelServiceImpl implements InventoryLevelService {

    private final InventoryLevelRepository inventoryLevelRepository;

    public InventoryLevelServiceImpl(InventoryLevelRepository inventoryLevelRepository) {
        this.inventoryLevelRepository = inventoryLevelRepository;
    }

    @Override
    public InventoryLevel saveInventory(InventoryLevel inventory) {
        return inventoryLevelRepository.save(inventory);
    }

    @Override
    public List<InventoryLevel> getAllInventory() {
        return inventoryLevelRepository.findAll();
    }

    @Override
    public InventoryLevel getInventoryById(Long id) {
        return inventoryLevelRepository.findById(id).orElse(null);
    }
}