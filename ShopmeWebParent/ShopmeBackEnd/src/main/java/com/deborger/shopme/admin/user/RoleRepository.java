package com.deborger.shopme.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.deborger.shopme.common.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {
}
