package com.example.levanhoa.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total_Amount")
    private double totalAmount;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;  // Thêm trường createdAt

    // Constructor mặc định
    public Order() {
        this.status = "pending";  // Giá trị mặc định
        this.createdAt = LocalDateTime.now();  // Gán giá trị mặc định khi tạo mới
    }

    // Constructor mới sửa lại với 6 tham số
    public Order(String code, String status, User user, double totalAmount, double shippingFee, double vat) {
        this.code = code;
        this.status = status;
        this.user = user;
        this.totalAmount = totalAmount + shippingFee + vat;  // Tính tổng tiền
        this.createdAt = LocalDateTime.now();  // Gán ngày tạo đơn hàng khi khởi tạo
    }

    // Getter và Setter cho các trường
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
