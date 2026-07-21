package com.example.shopapp.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.responses.ProductListResponse;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.services.IProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService iProductService;

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProduct(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createAt").descending());
        Page<ProductResponse> productPage = iProductService.getAllProducts(pageRequest);
        int totalPage = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(products)
                .totalPages(totalPage)
                .build();
        return ResponseEntity.ok(productListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            Product existingProduct = iProductService.getProductById(id);
            return ResponseEntity.ok(existingProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "", consumes = { "multipart/form-data" })
    public ResponseEntity<?> insertProduct(@Valid @ModelAttribute ProductDTO productDTO,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return bindingResult.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .reduce((message1, message2) -> message1 + ", " + message2)
                        .map(errorMessage -> ResponseEntity.badRequest().body(errorMessage))
                        .orElse(ResponseEntity.badRequest().body("Unknown error"));
            }
            MultipartFile file = productDTO.getFile();
            if (file != null) {
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size must be less than 10MB");
                }

                String contentType = file.getContentType();
                if (contentType == null ||
                        !(contentType.equals("image/jpeg") || contentType.equals("image/png")
                                || contentType.equals("image/gif"))) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File must be an image (jpg, png, gif)");
                }
                String fileName = storeFile(file);
                productDTO.setThumbnail(fileName);
            }
            Product product = iProductService.createProduct(productDTO);
            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
        try {
            iProductService.deleteProduct(id);
            return ResponseEntity.ok("Deleted product with id = " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @Valid @org.springframework.web.bind.annotation.RequestBody ProductDTO productDTO) {
        try {
            Product updatedProduct = iProductService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;
        java.nio.file.Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @ModelAttribute("files") List<MultipartFile> files,
            @PathVariable("product_id") Long productId) {
        try {
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body("Files must not be empty");
            }
            for (MultipartFile file : files) {
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size must be less than 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null ||
                        !(contentType.equals("image/jpeg") || contentType.equals("image/png")
                                || contentType.equals("image/gif"))) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File must be an image (jpg, png, gif)");
                }
                String fileName = storeFile(file);
                ProductImageDTO productImageDTO = new ProductImageDTO();
                productImageDTO.setProductId(productId);
                productImageDTO.setImageUrl(fileName);
                iProductService.createProductImage(productImageDTO);
            }
            return ResponseEntity.ok("Images uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
