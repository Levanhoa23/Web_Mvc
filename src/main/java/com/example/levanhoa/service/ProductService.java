package com.example.levanhoa.service;

import com.example.levanhoa.model.Product;
import com.example.levanhoa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable; // ✅ Dùng thư viện đúng
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

   @Autowired ProductRepository productRepository;

//goi repository
    public List<Product> getAllProducts(){

        return productRepository.findAll();
    }

    //
    public List<Product> searchProducts(String keyword){
        return productRepository.searchProducts(keyword);
    }

    public List<Product> getProductsByCategory(int categoryId){
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getProductById(int id){
        return productRepository.findById(id);
    }

    // Cập nhật sản phẩm
    public void updateProduct(Product product) {
    try {
        productRepository.save(product);  // Nếu sản phẩm có ID thì sẽ cập nhật, nếu không sẽ tạo mới
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    //delete san pham khi đuoc controller gọi đến
    public void deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
    }

    // Thêm sản phẩm
    public void addProduct(Product product) {
        try {
            productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();  // In ra lỗi nếu có
        }
    }

    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    //loc
    public List<Product> filterProductsByPrice(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    //phan trang
    public Page<Product> getProductsByPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return productRepository.findAll(pageable);
}

    public Page<Product> searchAndFilterProducts(String keyword, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.searchAndFilter(keyword, minPrice, maxPrice, pageable);
    }

}
