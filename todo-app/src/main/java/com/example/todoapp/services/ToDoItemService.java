package com.example.todoapp.services;

import com.example.todoapp.models.ToDoItem;
import com.example.todoapp.repositories.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import com.example.todoapp.repositories.ListCategoryRepository;

@Service
public class ToDoItemService {

    private final ToDoItemRepository toDoItemRepository;
    private final ListCategoryRepository listCategoryRepository;

    @Autowired
    public ToDoItemService(ToDoItemRepository toDoItemRepository, ListCategoryRepository listCategoryRepository) {
        this.toDoItemRepository = toDoItemRepository;
        this.listCategoryRepository = listCategoryRepository;
    }

    public Iterable<ToDoItem> getAll() {
        return toDoItemRepository.findAll();
    }

    public Optional<ToDoItem> getById(Long id) {
        return toDoItemRepository.findById(id);
    }

    public ToDoItem save(ToDoItem toDoItem) {
        if (toDoItem.getId() == null) {
            if (listCategoryRepository.count() == 0) {
                throw new IllegalStateException("At least one list category is required to create a todo item.");
            }
            toDoItem.setCreatedAt(Instant.now());
        }

        toDoItem.setUpdatedAt(Instant.now());
        return toDoItemRepository.save(toDoItem);
    }

    public void delete(ToDoItem toDoItem) {
        toDoItemRepository.delete(toDoItem);
    }

    public ToDoItem createToDoItemIfCategoryExists(ToDoItem toDoItem) {
        if (listCategoryRepository.count() > 0) {
            return save(toDoItem);
        } else {
            throw new IllegalStateException("No categories exist. Please create a category first.");
        }
    }

    public Iterable<ToDoItem> getByCategoryId(Long categoryId) {
        return toDoItemRepository.findAllByCategoryId(categoryId);
    }

    public void deleteByCategoryId(Long categoryId) {
        Iterable<ToDoItem> toDoItems = toDoItemRepository.findAllByCategoryId(categoryId);
        toDoItemRepository.deleteAll(toDoItems);
    }
}