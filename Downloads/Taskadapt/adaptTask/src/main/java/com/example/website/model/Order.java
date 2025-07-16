package com.example.website.model;



import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")                      // “order” is SQL keyword
public class Order {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private Instant createdAt = Instant.now();

    @ElementCollection
    private List<OrderLine> lines;

    // ----- constructors -----
    public Order() { }

    public Order(Long id, User user, Instant createdAt, List<OrderLine> lines) {
        this.id = id;
        this.user = user;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.lines = lines;
    }

    // ----- getters & setters -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<OrderLine> getLines() { return lines; }
    public void setLines(List<OrderLine> lines) { this.lines = lines; }

    /* ------------ embedded order‑line value object ------------ */
    @Embeddable
    public static class OrderLine {
        private Long productId;
        private String name;
        private double price;
        private int quantity;

        public OrderLine() { }

        public OrderLine(Long productId, String name, double price, int quantity) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        // getters & setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}

