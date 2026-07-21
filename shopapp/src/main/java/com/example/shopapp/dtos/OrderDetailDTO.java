package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's id must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's id must be > 0")
    private Long productId;

    @Min(value = 1, message = "Price must be > 0")
    private Long price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "Number of Products must be > 0")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 1, message = "Total money must be > 0")
    private int totalMoney;

    private String color;
}
