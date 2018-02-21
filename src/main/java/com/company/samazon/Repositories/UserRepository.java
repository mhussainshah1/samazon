package com.company.samazon.Repositories;

import com.company.samazon.Models.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long>{
    AppUser findByUsername(String username);
    AppUser findById(Long id);

}
