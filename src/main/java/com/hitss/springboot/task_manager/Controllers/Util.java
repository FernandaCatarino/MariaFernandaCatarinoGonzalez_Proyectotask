package com.hitss.springboot.task_manager.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class Util {
public static ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(e -> {
            errors.put(e.getField(), "El campo " + e.getField() + " " +  e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
