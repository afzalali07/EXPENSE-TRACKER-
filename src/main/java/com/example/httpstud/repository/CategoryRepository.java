package com.example.httpstud.repository;

import com.example.httpstud.model.Category;
import com.example.httpstud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserOrSystemCategoryTrue(User user);
    List<Category> findByUser(User user);
}
