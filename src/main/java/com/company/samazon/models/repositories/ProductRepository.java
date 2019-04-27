package com.company.samazon.models.repositories;

import com.company.samazon.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product findByName(String name);

    Iterable<Product> findAllByNameContainingIgnoreCase(String name);
}
