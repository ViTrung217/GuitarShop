package com.guitarshop.service;

import com.guitarshop.model.Category;
import com.guitarshop.repository.CategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Cacheable("categories")
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Cacheable(value = "category", key = "#id")
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = {"categories", "category"}, allEntries = true)
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @CacheEvict(value = {"categories", "category"}, allEntries = true)
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
