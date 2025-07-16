package com.example.website.controller;

import com.example.website.model.Order;
import com.example.website.repository.CartItemRepository;
import com.example.website.repository.OrderRepository;
import com.example.website.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CartItemRepository cartRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public OrderController(CartItemRepository cartRepo, OrderRepository orderRepo, UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @PostMapping
    public Order checkout(Authentication auth) {
    	
    	 System.out.println("Logged in user: " + auth.getName());
    	    System.out.println("Authorities: " + auth.getAuthorities());

        var user = userRepo.findByUsername(auth.getName());
        var items = cartRepo.findByUserId(user.getId());

        var lines = items.stream()
                .map(ci -> new Order.OrderLine(
                        ci.getProduct().getId(),
                        ci.getProduct().getName(),
                        ci.getProduct().getPrice(),
                        ci.getQuantity()))
                .toList();

        var order = new Order(null, user, null, lines);
        cartRepo.deleteByUserId(user.getId());
        return orderRepo.save(order);
    }

    @GetMapping
    public List<Order> myOrders(Authentication auth) {
        Long uid = userRepo.findByUsername(auth.getName()).getId();
        return orderRepo.findByUserId(uid);
    }
}
