package com.example.levanhoa.repository;

import com.example.levanhoa.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> { // ❌ Dùng Integer thay vì Long
    List<Cart> findByUserId(int userId);
}
