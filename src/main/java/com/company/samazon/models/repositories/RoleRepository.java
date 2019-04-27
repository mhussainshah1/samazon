package com.company.samazon.models.repositories;

import com.company.samazon.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
