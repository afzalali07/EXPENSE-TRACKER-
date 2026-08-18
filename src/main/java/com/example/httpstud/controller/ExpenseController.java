package com.example.httpstud.controller;

import com.example.httpstud.exception.ExpenseNotFoundException;
import com.example.httpstud.model.Category;
import com.example.httpstud.model.Expense;
import com.example.httpstud.model.User;
import com.example.httpstud.repository.CategoryRepository;
import com.example.httpstud.repository.ExpenseRepository;
import com.example.httpstud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private static final Path UPLOAD_DIR = Paths.get("uploads");

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseRepository expenseRepository,
                             CategoryRepository categoryRepository,
                             UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listExpenses(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Expense> expenses = expenseRepository.findByUserOrderByExpenseDateDesc(user);
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalAmount", expenses.stream().mapToDouble(Expense::getAmount).sum());
        return "expenses";
    }

    @GetMapping("/add")
    public String addExpenseForm(Model model) {
        Expense expense = new Expense();
        expense.setExpenseDate(LocalDate.now());
        model.addAttribute("expense", expense);
        model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(null));
        return "expense-form";
    }

    @PostMapping("/save")
    public String saveExpense(@Valid @ModelAttribute("expense") Expense expense,
                              BindingResult result,
                              @RequestParam("receiptFile") MultipartFile receiptFile,
                              Principal principal,
                              Model model) throws IOException {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(user));
            return "expense-form";
        }
        if (!Files.exists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR);
        }
        if (receiptFile != null && !receiptFile.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + receiptFile.getOriginalFilename();
            Path destination = UPLOAD_DIR.resolve(filename);
            receiptFile.transferTo(destination);
            expense.setReceiptFilename(filename);
        }
        if (expense.getCategory() != null && expense.getCategory().getId() != null) {
            expense.setCategory(categoryRepository.findById(expense.getCategory().getId()).orElse(null));
        }
        expense.setUser(user);
        expenseRepository.save(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/edit/{id}")
    public String editExpenseForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Expense expense = expenseRepository.findById(id)
                .filter(e -> e.getUser() != null && e.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + id + " not found"));
        model.addAttribute("expense", expense);
        model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(user));
        return "expense-form";
    }

    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable Long id,
                                @Valid @ModelAttribute("expense") Expense updatedExpense,
                                BindingResult result,
                                @RequestParam("receiptFile") MultipartFile receiptFile,
                                Principal principal,
                                Model model) throws IOException {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Expense expense = expenseRepository.findById(id)
                .filter(e -> e.getUser() != null && e.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + id + " not found"));

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findByUserOrSystemCategoryTrue(user));
            return "expense-form";
        }
        if (!Files.exists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR);
        }
        if (receiptFile != null && !receiptFile.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + receiptFile.getOriginalFilename();
            Path destination = UPLOAD_DIR.resolve(filename);
            receiptFile.transferTo(destination);
            updatedExpense.setReceiptFilename(filename);
        }
        expense.setTitle(updatedExpense.getTitle());
        if (updatedExpense.getCategory() != null && updatedExpense.getCategory().getId() != null) {
            expense.setCategory(categoryRepository.findById(updatedExpense.getCategory().getId()).orElse(null));
        }
        expense.setPaymentMethod(updatedExpense.getPaymentMethod());
        expense.setAmount(updatedExpense.getAmount());
        expense.setExpenseDate(updatedExpense.getExpenseDate());
        expense.setDescription(updatedExpense.getDescription());
        expense.setTags(updatedExpense.getTags());
        expense.setReceiptFilename(updatedExpense.getReceiptFilename());
        expense.setRecurring(updatedExpense.isRecurring());
        expense.setRecurrenceType(updatedExpense.getRecurrenceType());
        expense.setNextRecurringDate(updatedExpense.getNextRecurringDate());
        expenseRepository.save(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (!expenseRepository.existsByIdAndUser(id, user)) {
            throw new ExpenseNotFoundException("Cannot delete — Expense with ID " + id + " not found");
        }
        expenseRepository.deleteById(id);
        return "redirect:/expenses";
    }
}
