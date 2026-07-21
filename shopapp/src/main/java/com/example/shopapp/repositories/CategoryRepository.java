package com.example.shopapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopapp.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
