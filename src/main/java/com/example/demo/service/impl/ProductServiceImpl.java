package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Product createProduct(Product product) {
        return product;
    }

    @Override
    public void deactivateProduct(Long id) {
        // no-op
    }
}
