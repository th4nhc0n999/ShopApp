package com.example.shopapp.Controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.shopapp.dtos.ProductDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @GetMapping("")
    public ResponseEntity<String> getAllProduct (
        @RequestParam("page") int page,
        @RequestParam("limit") int limit
    )
    {
        return ResponseEntity.ok(String.format("page = %d, limit = %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") int id)
    {
        return ResponseEntity.ok("Product id = " + id);
    }

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity<?> insertProduct(@Valid @ModelAttribute ProductDTO productDTO,
        BindingResult bindingResult
     )
    {
        try {
            if(bindingResult.hasErrors())
        {
            return bindingResult.getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .reduce((message1, message2) -> message1 + ", " + message2)
            .map(errorMessage -> ResponseEntity.badRequest().body(errorMessage))
            .orElse(ResponseEntity.badRequest().body("Unknown error"));
        }
            MultipartFile file = productDTO.getFile();
            if(file != null)
        {
            if(file.getSize() > 10*1024*1024)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size must be less than 10MB");
        }

        String contentType = file.getContentType();
        if(contentType == null || 
            !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif")))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File must be an image (jpg, png, gif)");
        }
        String fileName = storeFile(file);
        }

        return ResponseEntity.ok("This is insertProduct ");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") int id)
    {
        return ResponseEntity.ok("Delete product id = " + id);
    }

    private String storeFile(MultipartFile file) throws IOException
    {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;
        java.nio.file.Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

}
