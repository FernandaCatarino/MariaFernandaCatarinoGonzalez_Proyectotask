package com.hitss.springboot.task_manager.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitss.springboot.task_manager.models.Task;



public interface taskRepository extends JpaRepository<Task,Long> {

  

}
