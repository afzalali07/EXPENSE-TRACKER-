package com.example.httpstud.controller;

import com.example.httpstud.model.Budget;
import com.example.httpstud.model.Category;
import com.example.httpstud.model.Expense;
import com.example.httpstud.model.Income;
import com.example.httpstud.model.User;
import com.example.httpstud.repository.BudgetRepository;
import com.example.httpstud.repository.CategoryRepository;
import com.example.httpstud.repository.ExpenseRepository;
import com.example.httpstud.repository.IncomeRepository;
import com.example.httpstud.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;

    public DashboardController(UserRepository userRepository,
                               ExpenseRepository expenseRepository,
                               IncomeRepository incomeRepository,
                               CategoryRepository categoryRepository,
                               BudgetRepository budgetRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Expense> expenses = expenseRepository.findByUserOrderByExpenseDateDesc(user);
        List<Income> incomes = incomeRepository.findByUserOrderByIncomeDateDesc(user);
        List<Category> categories = categoryRepository.findByUserOrSystemCategoryTrue(user);
        List<Budget> budgets = budgetRepository.findByUser(user);

        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double savings = totalIncome - totalExpense;

        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.getCategory() != null ? expense.getCategory().getName() : "Uncategorized",
                        Collectors.summingDouble(Expense::getAmount)));

        List<Expense> recentExpenses = expenses.stream().limit(6).collect(Collectors.toList());
        List<Income> recentIncomes = incomes.stream().limit(6).collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("savings", savings);
        model.addAttribute("recentExpenses", recentExpenses);
        model.addAttribute("recentIncomes", recentIncomes);
        model.addAttribute("categories", categories);
        model.addAttribute("budgets", budgets);
        model.addAttribute("categoryTotals", categoryTotals);

        return "dashboard";
    }
}
