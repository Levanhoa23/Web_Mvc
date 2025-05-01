package com.example.levanhoa.repository;

import com.example.levanhoa.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable; // ✅ Dùng thư viện đúng


import java.util.List;
//add san pham
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    //thao tac với csdl de hien thi san pham
    Product findById(int productId);
    List<Product> findByCategoryId(int categoryId);

    @Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")

    List<Product> searchProducts(@Param("keyword") String keyword);
//
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);


    //loc
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

//phan trang
Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
