package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.InventoryLevel;
import com.example.demo.entity.Store;
import com.example.demo.entity.Product;

@Repository
public interface InventoryLevelRepository extends JpaRepository<InventoryLevel, Long> {
    InventoryLevel findByStoreAndProduct(Store store, Product product); // keep if you have Store & Product objects
    List<InventoryLevel> findByStoreId(Long storeId);   
    List<InventoryLevel> findByProductId(Long productId); 
}
