package com.example.demo.service.impl;

import com.example.demo.service.InventoryBalancerService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InventoryBalancerServiceImpl implements InventoryBalancerService {

    @Override
    public List<String> generateTransferSuggestions() {
        // Stub implementation to keep build stable
        return Collections.emptyList();
    }
}
