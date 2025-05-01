package com.example.levanhoa.service;

import com.example.levanhoa.model.ProductVariant;
import com.example.levanhoa.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    // Tìm danh sách biến thể của sản phẩm theo ID sản phẩm
    public List<ProductVariant> findByProductId(Integer productId) {
        return productVariantRepository.findByProductId(productId);
    }
    public List<ProductVariant> findVariantsWithDifferentColor(int productId, String color) {
        return productVariantRepository.findByProductIdAndColorNot(productId, color);
    }
}

