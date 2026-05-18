package com.example.shopapp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopapp.dtos.OrderDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult)
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
            return ResponseEntity.ok("create order");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    } 

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId)
    {
        try {
            return ResponseEntity.ok("Lấy ra danh sách order từ user_id");
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
        @Valid @PathVariable Long id,
        @Valid @RequestBody OrderDTO orderDTO
    )
    {
        return ResponseEntity.ok("Cập nhật thông tin 1 order");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id) {
        return ResponseEntity.ok("Order delete successfully");
    }


}
