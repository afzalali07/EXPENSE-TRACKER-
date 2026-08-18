package com.example.httpstud.controller;

import com.example.httpstud.model.Budget;
import com.example.httpstud.model.Category;
import com.example.httpstud.model.User;
import com.example.httpstud.repository.BudgetRepository;
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
@RequestMapping("/budgets")
public class BudgetController {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public BudgetController(BudgetRepository budgetRepository,
                            CategoryRepository categoryRepository,
                            UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listBudgets(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Budget> budgets = budgetRepository.findByUser(user);
        model.addAttribute("budgets", budgets);
        return "budgets";
    }

    @GetMapping("/add")
    public String addBudgetForm(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("budget", new Budget());
        model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(user));
        return "budget-form";
    }

    @PostMapping("/save")
    public String saveBudget(@Valid @ModelAttribute Budget budget, BindingResult result, Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(user));
            return "budget-form";
        }
        if (budget.getCategory() != null && budget.getCategory().getId() != null) {
            budget.setCategory(categoryRepository.findById(budget.getCategory().getId()).orElse(null));
        } else {
            budget.setCategory(null);
        }
        budget.setUser(user);
        budgetRepository.save(budget);
        return "redirect:/budgets";
    }

    @GetMapping("/edit/{id}")
    public String editBudgetForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Budget budget = budgetRepository.findById(id).orElseThrow();
        if (!budget.getUser().getId().equals(user.getId())) {
            return "redirect:/budgets";
        }
        model.addAttribute("budget", budget);
        model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(user));
        return "budget-form";
    }

    @PostMapping("/update/{id}")
    public String updateBudget(@PathVariable Long id, @Valid @ModelAttribute Budget budget, BindingResult result, Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(user));
            return "budget-form";
        }
        Budget existing = budgetRepository.findById(id).orElseThrow();
        existing.setTargetAmount(budget.getTargetAmount());
        existing.setPeriodType(budget.getPeriodType());
        existing.setRollover(budget.isRollover());
        if (budget.getCategory() != null && budget.getCategory().getId() != null) {
            existing.setCategory(categoryRepository.findById(budget.getCategory().getId()).orElse(null));
        } else {
            existing.setCategory(null);
        }
        budgetRepository.save(existing);
        return "redirect:/budgets";
    }

    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Budget budget = budgetRepository.findById(id).orElseThrow();
        if (!budget.getUser().getId().equals(user.getId())) {
            return "redirect:/budgets";
        }
        budgetRepository.delete(budget);
        return "redirect:/budgets";
    }
}
