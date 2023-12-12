package com.example.todoapp.services;

import com.example.todoapp.models.ListCategory;
import com.example.todoapp.repositories.ListCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListCategoryService {

    private final ListCategoryRepository listCategoryRepository;
    private final ToDoItemService toDoItemService; // Injected ToDoItemService

    @Autowired
    public ListCategoryService(ListCategoryRepository listCategoryRepository,
                               ToDoItemService toDoItemService) { // Constructor injection
        this.listCategoryRepository = listCategoryRepository;
        this.toDoItemService = toDoItemService; // Assigning the injected service
    }

    public ListCategory createCategory(String name) {
        ListCategory category = new ListCategory();
        category.setName(name);
        return listCategoryRepository.save(category);
    }

    public Iterable<ListCategory> getAllCategories() {
        return listCategoryRepository.findAll();
    }

    public Optional<ListCategory> getCategoryById(Long id) {
        return listCategoryRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return listCategoryRepository.existsById(id);
    }

    @Transactional
    public void deleteCategoryAndTodos(Long categoryId) {

        if (!listCategoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category with id " + categoryId + " does not exist.");
        }
        toDoItemService.deleteByCategoryId(categoryId);

        listCategoryRepository.deleteById(categoryId);
    }

}