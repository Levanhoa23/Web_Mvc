package com.example.levanhoa.dto;

import java.time.LocalDateTime;

public class OrderWithItemsDTO {
    private String orderId;
    private String orderCode;
    private String orderStatus;
    private int userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private int orderItemId;
    private int productId;
    private int quantity;
    private double itemPrice;
    private LocalDateTime itemCreatedAt;

    // Constructor
    public OrderWithItemsDTO(String orderId, String orderCode, String orderStatus, int userId, LocalDateTime orderDate, double totalAmount,
                             int orderItemId, int productId, int quantity, double itemPrice, LocalDateTime itemCreatedAt) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.itemCreatedAt = itemCreatedAt;
    }

    // Getters and setters


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public LocalDateTime getItemCreatedAt() {
        return itemCreatedAt;
    }

    public void setItemCreatedAt(LocalDateTime itemCreatedAt) {
        this.itemCreatedAt = itemCreatedAt;
    }
}
