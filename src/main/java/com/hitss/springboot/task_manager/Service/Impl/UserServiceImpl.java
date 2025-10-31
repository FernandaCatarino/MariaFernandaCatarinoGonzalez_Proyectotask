package com.hitss.springboot.task_manager.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitss.springboot.task_manager.Repositories.RoleRepository;
import com.hitss.springboot.task_manager.Repositories.UserRepository;
import com.hitss.springboot.task_manager.Service.UserService;
import com.hitss.springboot.task_manager.models.Role;
import com.hitss.springboot.task_manager.models.User;


@Service
public class UserServiceImpl  implements UserService{

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User save(User user) {


        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("El password es requerido");
        }

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        } else {
            user.getRoles().clear(); 
        }
        
        
        Optional<Role> optionalUserRole = roleRepository.findByName("USER");
        if (optionalUserRole.isPresent()) {
            user.getRoles().add(optionalUserRole.get());
        }

        
        if (user.isAdmin()) {
            Optional<Role> optionalAdminRole = roleRepository.findByName("ADMIN");
            if (optionalAdminRole.isPresent()) {
                user.getRoles().add(optionalAdminRole.get());
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.saveAndFlush(user);
        
       
        User reloadedUser = userRepository.findById(savedUser.getId())
            .orElse(savedUser);
        
        
        if (reloadedUser.getRoles() != null) {
            reloadedUser.getRoles().size(); 
        }
        
        return reloadedUser;
    }

  
 @Transactional(readOnly = true)
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
}
