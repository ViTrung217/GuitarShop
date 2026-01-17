package com.guitarshop.service;

import com.guitarshop.model.Product;
import com.guitarshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);
            Files.copy(image.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            product.setImagePath("/uploads/" + fileName);
        }
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
