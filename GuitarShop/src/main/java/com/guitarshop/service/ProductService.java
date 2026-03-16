package com.guitarshop.service;

import com.guitarshop.model.Product;
import com.guitarshop.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable("products")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Cacheable(value = "product", key = "#id")
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
