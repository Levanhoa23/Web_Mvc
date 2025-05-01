package com.example.levanhoa.service;

import com.example.levanhoa.model.Order;
import com.example.levanhoa.model.OrderItem;
import com.example.levanhoa.repository.OrderItemRepository;
import com.example.levanhoa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Order getOrderById(int id){
        return orderRepository.findById(id);
    }
    public Order addOrder(Order order){
        orderRepository.save(order);
        return order;
    }
    public Order updateOrder(int id, Order order){
        Order existingOrder = orderRepository.findById(id);
        if (existingOrder != null){
            order.setId(id);
            orderRepository.save(order);
            return order;
        }
        return null;
    }
    public void createOrder(Order order, List<OrderItem> items) {
        double totalAmount = 0;

        // Tính tổng tiền từ các OrderItem
        for (OrderItem item : items) {
            totalAmount += item.getPrice() * item.getQuantity();  // Tính tổng cho từng item
            item.setOrder(order);  // Gắn order_id vào từng order_item
            orderItemRepository.save(item);  // Lưu từng sản phẩm trong đơn hàng
        }

        order.setTotalAmount(totalAmount);  // Cập nhật tổng tiền vào đơn hàng
        orderRepository.save(order);  // Lưu đơn hàng với tổng tiền vào cơ sở dữ liệu
    }


    public void deleteOrder(int id){
        Order existingOrder = orderRepository.findById(id);
        if (existingOrder != null){
            orderRepository.deleteById(id);
        }
    }
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        Order order = getOrderById(orderId);
        return order != null ? orderItemRepository.findByOrderId(orderId) : new ArrayList<>();
    }
}
