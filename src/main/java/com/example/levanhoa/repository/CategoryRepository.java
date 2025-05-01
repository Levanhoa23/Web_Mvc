package com.example.levanhoa.repository;

import com.example.levanhoa.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findById(int id);
}