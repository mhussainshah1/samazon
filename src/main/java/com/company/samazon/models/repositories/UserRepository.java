package com.company.samazon.models.repositories;

import com.company.samazon.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
//    User findById(Long id);

}
