package com.example.website.model;



import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne private User user;
    @ManyToOne private Product product;
    private int quantity;

    public CartItem() { }

    public CartItem(Long id, User user, Product product, int quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

