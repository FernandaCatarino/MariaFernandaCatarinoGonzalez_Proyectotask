package com.hitss.springboot.task_manager.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.hitss.springboot.task_manager.Service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String>{

    @Autowired
    private UserService userService;



    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userService == null) {
            return true;
        }
        return !userService.existsByUsername(username);
    }
}