package com.company.samazon.Repositories;

import com.company.samazon.Models.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CartRepository extends CrudRepository<Cart, Long> {
//    Collection<Cart> findByAppuserByUsername(String username);



}
