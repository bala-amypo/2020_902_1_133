package com.example.demo.service;

import com.example.demo.entity.Product;

public interface ProductService {

    Product createProduct(Product product);

    void deactivateProduct(Long id);
}
