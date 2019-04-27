package com.company.samazon.service;

import com.company.samazon.models.Product;
import com.company.samazon.models.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(long id) {
        return productRepository.findById(id).get();
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    public Iterable<Product> searchProducts(String query) {
        return productRepository.findAllByNameContainingIgnoreCase(query);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateQuantity(Product product) {
        product.setQuantity(product.getQuantity() - 1);
    }

}
