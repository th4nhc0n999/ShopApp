package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    @Min(value = 1, message = "Product ID must be at least 1")
    @JsonProperty("product_id")
    private Long productId;

    @Size(min = 5, max = 200, message = "Image URL must be at least 5 characters long and at most 200 characters long")
    @JsonProperty("image_url")
    private String imageUrl;
}
