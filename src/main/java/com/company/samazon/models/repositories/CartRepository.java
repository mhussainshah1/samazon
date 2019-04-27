package com.company.samazon.models.repositories;

import com.company.samazon.models.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {
//    Collection<Cart> findByUserByUsername(String username);
}
