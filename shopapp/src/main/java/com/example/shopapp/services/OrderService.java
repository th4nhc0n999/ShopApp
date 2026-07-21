package com.example.shopapp.services;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        // Tìm xem user's id có tồn tại ko
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        // Convert orderDTO => Order
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("pending");
        order.setActive(true);

        // Lưu vào DB
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
        
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));
        
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // Không xóa cứng, xóa mềm
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        // Need to add this method in repository
        return orderRepository.findByUserId(userId);
    }
}
