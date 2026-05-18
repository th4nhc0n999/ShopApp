package com.example.shopapp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopapp.dtos.CategoryDTO;

import jakarta.validation.Valid;



// @Validated
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @GetMapping("")
    public ResponseEntity<String> getAllCategory (
        @RequestParam("page") int page,
        @RequestParam("limit") int limit
    )
    {
        return ResponseEntity.ok(String.format("page = %d, limit = %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCategoryById(@PathVariable("id") int id)
    {
        return ResponseEntity.ok("Category id = " + id);
    }
    @PostMapping("")
    public ResponseEntity<String> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO,
    BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return bindingResult.getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .reduce((message1, message2) -> message1 + ", " + message2)
            .map(errorMessage -> ResponseEntity.badRequest().body(errorMessage))
            .orElse(ResponseEntity.badRequest().body("Unknown error"));
        }
        return ResponseEntity.ok("This is insertCategory" + categoryDTO.toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") int id)
    {
        return ResponseEntity.ok("Delete category id = " + id);
    }

}
