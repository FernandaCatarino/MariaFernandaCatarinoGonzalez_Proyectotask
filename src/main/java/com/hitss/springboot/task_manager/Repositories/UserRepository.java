package com.hitss.springboot.task_manager.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitss.springboot.task_manager.models.User;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
