package com.example.todoapp.repositories;

import com.example.todoapp.models.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
    List<ToDoItem> findAllByCategoryId(Long categoryId);
}
