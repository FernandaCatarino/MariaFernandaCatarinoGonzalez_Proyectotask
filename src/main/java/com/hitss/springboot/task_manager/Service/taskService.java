package com.hitss.springboot.task_manager.Service;

import java.util.List;
import java.util.Optional;


import com.hitss.springboot.task_manager.models.Task;




public interface taskService {

    List<Task> findAll();

    Optional<Task> findById(Long id);

    Task save(Task product);

    Optional<Task> update(Long id, Task product);

    Optional<Task> delete(Long id);

    

}
