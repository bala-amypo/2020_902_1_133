package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Product createProduct(Product product) {
        return product;
    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public void deactivateProduct(Long id) {
        // no-op
    }
}
