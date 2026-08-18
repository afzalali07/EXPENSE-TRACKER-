package com.example.httpstud.repository;

import com.example.httpstud.model.Budget;
import com.example.httpstud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);
}
