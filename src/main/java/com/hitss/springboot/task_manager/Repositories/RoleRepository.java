package com.hitss.springboot.task_manager.Repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitss.springboot.task_manager.models.Role;


public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
