package com.example.website.controller;

import com.example.website.model.CartItem;
import com.example.website.repository.CartItemRepository;
import com.example.website.repository.ProductRepository;
import com.example.website.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public CartController(CartItemRepository cartRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<CartItem> myCart(Authentication auth) {
        Long uid = userRepo.findByUsername(auth.getName()).getId();
        return cartRepo.findByUserId(uid);
    }

    @PostMapping("/add")
    public CartItem add(Authentication auth, @RequestParam Long productId, @RequestParam int qty) {
        var user = userRepo.findByUsername(auth.getName());
        var product = productRepo.findById(productId).orElseThrow();
        return cartRepo.save(new CartItem(null, user, product, qty));
    }

    @DeleteMapping("/remove/{itemId}")
    public void remove(@PathVariable Long itemId) {
        cartRepo.deleteById(itemId);
    }

    @DeleteMapping("/clear")
    public void clear(Authentication auth) {
        Long uid = userRepo.findByUsername(auth.getName()).getId();
        cartRepo.deleteByUserId(uid);
    }
}
