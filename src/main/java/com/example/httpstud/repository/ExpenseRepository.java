package com.example.httpstud.repository;

import com.example.httpstud.model.Expense;
import com.example.httpstud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserOrderByExpenseDateDesc(User user);
    List<Expense> findTop6ByUserOrderByExpenseDateDesc(User user);
    boolean existsByIdAndUser(Long id, User user);
}

