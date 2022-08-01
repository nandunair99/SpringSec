package com.narola.springSecurityJPA.repository;

import com.narola.springSecurityJPA.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {
    User findByUsername(String username);
}
