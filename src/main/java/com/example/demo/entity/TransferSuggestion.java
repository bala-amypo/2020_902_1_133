// package com.example.demo.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "transfer_suggestions")
// public class TransferSuggestion {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
    
//     @ManyToOne
//     @JoinColumn(name = "source_store_id")
//     private Store sourceStore;
    
//     @ManyToOne
//     @JoinColumn(name = "target_store_id")
//     private Store targetStore;
    
//     @ManyToOne
//     @JoinColumn(name = "product_id", nullable = false)
//     private Product product;
    
//     @Column(nullable = false)
//     private Integer quantity;
    
//     @Column(nullable = false)
//     private String priority = "PENDING";
    
//     @Column(name = "generated_at")
//     private LocalDateTime generatedAt;
    
//     @Column(nullable = false)
//     private String status = "PENDING";
    
//     @Column(name = "suggested_quantity")
//     private Integer suggestedQuantity;
    
//     @Column(name = "reason")
//     private String reason;
    
//     @PrePersist
//     protected void onCreate() {
//         generatedAt = LocalDateTime.now();
//     }
    
//     // Getters and Setters
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
    
//     public Store getSourceStore() { return sourceStore; }
//     public void setSourceStore(Store sourceStore) { this.sourceStore = sourceStore; }
    
//     public Store getTargetStore() { return targetStore; }
//     public void setTargetStore(Store targetStore) { this.targetStore = targetStore; }
    
//     public Product getProduct() { return product; }
//     public void setProduct(Product product) { this.product = product; }
    
//     public Integer getQuantity() { return quantity; }
//     public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
//     public String getPriority() { return priority; }
//     public void setPriority(String priority) { this.priority = priority; }
    
//     public LocalDateTime getGeneratedAt() { return generatedAt; }
//     public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    
//     public String getStatus() { return status; }
//     public void setStatus(String status) { this.status = status; }
    
//     public Integer getSuggestedQuantity() { return suggestedQuantity; }
//     public void setSuggestedQuantity(Integer suggestedQuantity) { this.suggestedQuantity = suggestedQuantity; }
    
//     public String getReason() { return reason; }
//     public void setReason(String reason) { this.reason = reason; }
    
//     public LocalDateTime getGeneratedAt() { return suggestedAt; }
// }


package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_suggestions")
public class TransferSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_store_id")
    private Store sourceStore;

    @ManyToOne
    @JoinColumn(name = "target_store_id")
    private Store targetStore;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String priority = "PENDING";

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "suggested_quantity")
    private Integer suggestedQuantity;

    @Column(name = "reason")
    private String reason;

    // Automatically set generatedAt before insert
    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getSourceStore() {
        return sourceStore;
    }

    public void setSourceStore(Store sourceStore) {
        this.sourceStore = sourceStore;
    }

    public Store getTargetStore() {
        return targetStore;
    }

    public void setTargetStore(Store targetStore) {
        this.targetStore = targetStore;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSuggestedQuantity() {
        return suggestedQuantity;
    }

    public void setSuggestedQuantity(Integer suggestedQuantity) {
        this.suggestedQuantity = suggestedQuantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
