package com.example.demo.service.impl;

import com.example.demo.entity.Store;
import com.example.demo.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Override
    public Store createStore(Store store) {
        return store;
    }

    @Override
    public Store updateStore(Long id, Store store) {
        return store;
    }

    @Override
    public List<Store> getAllStores() {
        return List.of();
    }

    @Override
    public Store getStoreById(Long id) {
        return null;
    }

    @Override
    public void deleteStore(Long id) {
        // no-op
    }
}
