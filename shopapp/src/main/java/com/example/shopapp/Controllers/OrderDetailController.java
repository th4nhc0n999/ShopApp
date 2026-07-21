package com.example.shopapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopapp.dtos.OrderDetailDTO;

import jakarta.validation.Valid;
import lombok.val;

import com.example.shopapp.services.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import com.example.shopapp.models.OrderDetail;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO newOrderDetail) {
        try {
            OrderDetail orderDetail = orderDetailService.createOrderDetail(newOrderDetail);
            return ResponseEntity.ok(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id) {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId) {
        try {
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO newOrderDetailData) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, newOrderDetailData);
            return ResponseEntity.ok(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok("OrderDetail deleted successfully");
    }
}
