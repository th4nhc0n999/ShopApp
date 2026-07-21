package com.example.shopapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.services.CategoryService;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// @Validated
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .map(errorMessage -> ResponseEntity.badRequest().body(errorMessage))
                    .orElse(ResponseEntity.badRequest().body("Unknown error"));
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("This is insertCategory" + categoryDTO.toString());
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategory(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category id = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category id = " + id);
    }

}
