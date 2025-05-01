package com.example.levanhoa.repository;

import com.example.levanhoa.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findById(int id);
    List<Order> findByUserId(int userId);


    long count();  // Đảm bảo trả về tổng số đơn hàng

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double sumOrderAmount();  // Đảm bảo trả về tổng số tiền tất cả đơn hàng

    long countByStatus(String status);  // Đảm bảo trả về số lượng đơn hàng theo trạng thái

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = :status")
    Double sumAmountByStatus(@Param("status") String status);  // Sử dụng Double thay vì double


    //query hai bang order va orderitem de lay thong tin ve don hang

}