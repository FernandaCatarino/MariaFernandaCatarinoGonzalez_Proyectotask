package com.hitss.springboot.task_manager.Service;

import java.util.List;

import com.hitss.springboot.task_manager.models.User;



public  interface UserService {

     List<User>findAll();

     User save(User user);

    public boolean existsByUsername(String username);

    

    
}
