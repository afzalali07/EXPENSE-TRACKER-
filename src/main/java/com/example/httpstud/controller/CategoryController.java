package com.example.httpstud.controller;

import com.example.httpstud.model.Category;
import com.example.httpstud.model.User;
import com.example.httpstud.repository.CategoryRepository;
import com.example.httpstud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryController(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listCategories(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Category> categories = categoryRepository.findByUserOrSystemCategoryTrue(user);
        model.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/add")
    public String addCategoryForm(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("category", new Category());
        model.addAttribute("parentCategories", categoryRepository.findByUserOrSystemCategoryTrue(user));
        return "category-form";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute Category category, BindingResult result, Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (result.hasErrors()) {
            model.addAttribute("parentCategories", categoryRepository.findByUserOrSystemCategoryTrue(user));
            return "category-form";
        }
        if (category.getParentCategory() != null && category.getParentCategory().getId() != null) {
            category.setParentCategory(categoryRepository.findById(category.getParentCategory().getId()).orElse(null));
        } else {
            category.setParentCategory(null);
        }
        category.setUser(user);
        category.setSystemCategory(false);
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Category category = categoryRepository.findById(id).orElseThrow();
        if (category.getUser() != null && !category.getUser().getId().equals(user.getId())) {
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("parentCategories", categoryRepository.findByUserOrSystemCategoryTrue(user));
        return "category-form";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute Category category, BindingResult result, Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (result.hasErrors()) {
            model.addAttribute("parentCategories", categoryRepository.findByUserOrSystemCategoryTrue(user));
            return "category-form";
        }
        Category existing = categoryRepository.findById(id).orElseThrow();
        existing.setName(category.getName());
        existing.setColor(category.getColor());
        existing.setIcon(category.getIcon());
        existing.setSpendingLimit(category.getSpendingLimit());
        if (category.getParentCategory() != null && category.getParentCategory().getId() != null) {
            existing.setParentCategory(categoryRepository.findById(category.getParentCategory().getId()).orElse(null));
        } else {
            existing.setParentCategory(null);
        }
        categoryRepository.save(existing);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Category category = categoryRepository.findById(id).orElseThrow();
        if (category.getUser() == null || !category.getUser().getId().equals(user.getId())) {
            return "redirect:/categories";
        }
        categoryRepository.delete(category);
        return "redirect:/categories";
    }
}
