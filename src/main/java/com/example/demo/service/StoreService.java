package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Store;

public interface StoreService {

    Store saveStore(Store store);

    List<Store> getAllStores();

    Store getStoreById(Long id);
}