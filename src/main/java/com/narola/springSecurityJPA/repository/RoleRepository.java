package com.narola.springSecurityJPA.repository;

import com.narola.springSecurityJPA.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String roleName);
}
