package com.example.todoapp.controllers;

import com.example.todoapp.services.ListCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/categories")
public class ListCategoryController {

    private final ListCategoryService listCategoryService;


    @Autowired
    public ListCategoryController(ListCategoryService listCategoryService) {
        this.listCategoryService = listCategoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", listCategoryService.getAllCategories());
        return "index";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam("categoryName") String categoryName) {
        listCategoryService.createCategory(categoryName);
        return "redirect:/";
    }

    @GetMapping("/selectCategory/{id}")
    public String selectCategory(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (listCategoryService.getCategoryById(id).isPresent()) {
            session.setAttribute("selectedCategoryId", id);
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Category not found.");
            return "redirect:/categories";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            listCategoryService.deleteCategoryAndTodos(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category and its todos deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting category: " + e.getMessage());
        }
        return "redirect:/categories";
    }
}
