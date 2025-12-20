package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.InventoryLevel;

public interface InventoryLevelService {

    InventoryLevel saveInventory(InventoryLevel inventoryLevel);

    List<InventoryLevel> getAllInventory();

    InventoryLevel getInventoryById(Long id);
}