package com.example.todoapp.controllers;

import com.example.todoapp.models.ToDoItem;
import com.example.todoapp.services.ListCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import com.example.todoapp.services.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @Autowired
    private ToDoItemService toDoItemService;
    @Autowired
    private ListCategoryService listCategoryService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Long selectedCategoryId = (Long) session.getAttribute("selectedCategoryId");

        Iterable<ToDoItem> toDoItems;
        if (selectedCategoryId != null) {
            toDoItems = toDoItemService.getByCategoryId(selectedCategoryId);
        } else {
            toDoItems = toDoItemService.getAll();
        }

        model.addAttribute("toDoItems", toDoItems);
        model.addAttribute("categories", listCategoryService.getAllCategories());

        return "index";
    }
}
