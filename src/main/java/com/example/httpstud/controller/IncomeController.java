package com.example.httpstud.controller;

import com.example.httpstud.model.Income;
import com.example.httpstud.model.User;
import com.example.httpstud.repository.IncomeRepository;
import com.example.httpstud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeController(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listIncomes(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Income> incomes = incomeRepository.findByUserOrderByIncomeDateDesc(user);
        model.addAttribute("incomes", incomes);
        model.addAttribute("totalIncome", incomes.stream().mapToDouble(Income::getAmount).sum());
        return "incomes";
    }

    @GetMapping("/add")
    public String addIncomeForm(Model model) {
        Income income = new Income();
        income.setIncomeDate(LocalDate.now());
        model.addAttribute("income", income);
        return "income-form";
    }

    @PostMapping("/save")
    public String saveIncome(@Valid @ModelAttribute Income income, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "income-form";
        }
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        income.setUser(user);
        incomeRepository.save(income);
        return "redirect:/incomes";
    }

    @GetMapping("/edit/{id}")
    public String editIncomeForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Income income = incomeRepository.findById(id)
                .filter(i -> i.getUser() != null && i.getUser().getId().equals(user.getId()))
                .orElseThrow();
        model.addAttribute("income", income);
        return "income-form";
    }

    @PostMapping("/update/{id}")
    public String updateIncome(@PathVariable Long id, @Valid @ModelAttribute Income updatedIncome, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "income-form";
        }
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Income income = incomeRepository.findById(id)
                .filter(i -> i.getUser() != null && i.getUser().getId().equals(user.getId()))
                .orElseThrow();
        income.setSource(updatedIncome.getSource());
        income.setAmount(updatedIncome.getAmount());
        income.setIncomeDate(updatedIncome.getIncomeDate());
        income.setType(updatedIncome.getType());
        income.setNotes(updatedIncome.getNotes());
        incomeRepository.save(income);
        return "redirect:/incomes";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Income income = incomeRepository.findById(id)
                .filter(i -> i.getUser() != null && i.getUser().getId().equals(user.getId()))
                .orElseThrow();
        incomeRepository.delete(income);
        return "redirect:/incomes";
    }
}
