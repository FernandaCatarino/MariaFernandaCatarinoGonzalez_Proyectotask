package com.hitss.springboot.task_manager.Service.Impl;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitss.springboot.task_manager.Repositories.taskRepository;
import com.hitss.springboot.task_manager.Service.taskService;
import com.hitss.springboot.task_manager.models.Task;

@Service
public class taskServiceImpl  implements taskService{
                private taskRepository taskRepository;


    public taskServiceImpl(com.hitss.springboot.task_manager.Repositories.taskRepository taskRepository) {
                    this.taskRepository = taskRepository;
                }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    @Override
    public Task save(Task tasks) {
        return taskRepository.save(tasks);
    }

    @Transactional
    @Override
    public Optional<Task> update(Long id, Task tasks) {
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isPresent()) {
            Task taskDb = optional.orElseThrow();

            taskDb.setTitle(tasks.getTitle());
            taskDb.setDescription(tasks.getDescription());
            taskDb.setStatus((tasks.getStatus()));

            return Optional.of(taskRepository.save(taskDb));
        }
        return optional;
    }
    
    @Transactional
    @Override
    public Optional<Task> delete(Long id) {
        Optional<Task> optional = taskRepository.findById(id);
        optional.ifPresent(p -> taskRepository.deleteById(id));
        return optional;
    }

   
}
