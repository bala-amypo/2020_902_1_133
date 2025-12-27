package com.example.demo.service.impl;

import com.example.demo.entity.Store;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.StoreService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    
    private final StoreRepository storeRepository;
    
    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
    
    @Override
    public Store createStore(Store store) {
        if (storeRepository.findByStoreName(store.getStoreName()) != null) {
            throw new BadRequestException("Store name already exists");
        }
        store.setActive(true);
        return storeRepository.save(store);
    }
    
    @Override
    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
    }
    
    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
    
    @Override
    public Store updateStore(Long id, Store update) {
        Store existing = getStoreById(id);
        existing.setStoreName(update.getStoreName());
        existing.setAddress(update.getAddress());
        existing.setRegion(update.getRegion());
        if (update.isActive() != null) {
            existing.setActive(update.isActive());
        }
        return storeRepository.save(existing);
    }
    
    @Override
    public void deactivateStore(Long id) {
        Store store = getStoreById(id);
        store.setActive(false);
        storeRepository.save(store);
    }
}