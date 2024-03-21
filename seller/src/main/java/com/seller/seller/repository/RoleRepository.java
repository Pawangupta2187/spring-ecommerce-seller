package com.seller.seller.repository;

import com.seller.seller.entities.users.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    List<Role> findByAuthority(String authority);
}
