package com.example.todoapp.controllers;

import com.example.todoapp.models.ListCategory;
import com.example.todoapp.services.ListCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.todoapp.services.ToDoItemService;
import com.example.todoapp.models.ToDoItem;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.util.Optional;


@Controller
public class ToDoFormController {

    @Autowired
    private ToDoItemService toDoItemService;
    @Autowired
    private ListCategoryService listCategoryService;

    @GetMapping("/create-todo")
    public String showCreateForm(Model model, HttpSession session) {
        Long selectedCategoryId = (Long) session.getAttribute("selectedCategoryId");
        if (selectedCategoryId == null) {
            return "redirect:/categories";
        }
        model.addAttribute("toDoItem", new ToDoItem());
        model.addAttribute("selectedCategoryId", selectedCategoryId);
        return "new-todo-item";
    }


    @GetMapping("/delete/{id}")
    public String deleteToDoItem(@PathVariable("id") Long id, Model model) {
        toDoItemService.getById(id).ifPresentOrElse(
                toDoItemService::delete,
                () -> {
                    throw new IllegalArgumentException("ToDoItem id: " + id + " not found");
                }
        );
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ToDoItem toDoItem = toDoItemService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("ToDoItem id: " + id + " not found"));
        model.addAttribute("toDoItem", toDoItem);
        return "edit-todo-item";
    }

    @PostMapping("/todo")
    public String createToDoItem(@Valid ToDoItem toDoItem, BindingResult result, Model model, @RequestParam("categoryId") Long categoryId) {
        if (result.hasErrors()) {
            // handle errors
            return "new-todo-item";
        }

        // Retrieve the category by ID
        Optional<ListCategory> categoryOpt = listCategoryService.getCategoryById(categoryId);
        if (!categoryOpt.isPresent()) {
            return "new-todo-item";
        }

        toDoItem.setCategory(categoryOpt.get());

        toDoItem.setCreatedAt(Instant.now());
        toDoItem.setUpdatedAt(Instant.now());

        toDoItemService.save(toDoItem);

        return "redirect:/";
    }

    @PostMapping("/todo/{id}")
    public String updateToDoItem(@PathVariable("id") Long id, @Valid ToDoItem toDoItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("toDoItem", toDoItem);
            return "edit-todo-item";
        }

        ToDoItem existingToDoItem = toDoItemService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ToDoItem Id:" + id));

        existingToDoItem.setDescription(toDoItem.getDescription());
        existingToDoItem.setIsComplete(toDoItem.getIsComplete());
        existingToDoItem.setUpdatedAt(Instant.now());

        toDoItemService.save(existingToDoItem);

        return "redirect:/";
    }
}
