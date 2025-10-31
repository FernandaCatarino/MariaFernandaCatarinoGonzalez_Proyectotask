package com.hitss.springboot.task_manager.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitss.springboot.task_manager.Repositories.UserRepository;
import com.hitss.springboot.task_manager.Service.taskService;
import com.hitss.springboot.task_manager.models.Task;
import com.hitss.springboot.task_manager.models.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/tasks")
@Tag(
    name = "Tasks",
    description = "Endpoints for task "+
    "Includes operations for listing, creating, updating and deleting tasks."
)
public class TaskController {
    private taskService taskService;
    private UserRepository userRepository;

    public TaskController(taskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }
    
   @Operation(
        summary = "Get all tasks",
        description = "Retrieves all registered tasks"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Task> list(){
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        Optional<Task> optionalt= taskService.findById(id);
        if (optionalt.isPresent()) {
            return ResponseEntity.ok(optionalt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> create(@Valid @RequestBody Task task,
                    BindingResult result,
                    Authentication authentication){
        if (result.hasFieldErrors()) {
            return Util.validation(result);
        }
        
        task.setId(null);
        
       
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            task.setUser(userOptional.get());
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(taskService.save(task));
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update( 
    @Valid @RequestBody Task product, 
    BindingResult result,
    @PathVariable Long id){
        if (result.hasFieldErrors()) {
            return Util.validation(result);
        }
        Optional<Task> optional = taskService.update(id, product);
        if (optional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Task> optional = taskService.delete(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}