package com.company.samazon.Repositories;

import com.company.samazon.Models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRoleName(String roleName);

}
