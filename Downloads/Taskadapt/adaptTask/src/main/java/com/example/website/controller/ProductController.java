package com.example.website.controller;

import com.example.website.model.Product;
import com.example.website.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    // ✅ Basic pagination
    @GetMapping
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    // ✅ Admin-only: Add new product
    @PostMapping
    public Product add(@RequestBody Product p, Authentication auth) {
        System.out.println("Logged in as: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        return productRepo.save(p);
    }

    // ✅ Search by name and/or category
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {

        if (name != null && category != null) {
            return productRepo.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(name, category);
        } else if (name != null) {
            return productRepo.findByNameContainingIgnoreCase(name);
        } else if (category != null) {
            return productRepo.findByCategoryIgnoreCase(category);
        } else {
            return productRepo.findAll();
        }
    }

    // ✅ Admin-only: Update product
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product p) {
        p.setId(id);
        return productRepo.save(p);
    }

    // ✅ Admin-only: Delete product
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productRepo.deleteById(id);
    }
}
