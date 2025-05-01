package com.example.levanhoa.repository;

import com.example.levanhoa.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> findByProductIdAndColorNot(int productId, String color);
    // Tìm danh sách biến thể của sản phẩm theo product_id
    List<ProductVariant> findByProductId(Integer productId);
}
