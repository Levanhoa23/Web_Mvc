package com.example.levanhoa.service;

import com.example.levanhoa.model.Category;
import com.example.levanhoa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);  // Trả về đối tượng Category nếu tìm thấy
    }
    public List<Category> findAll() {
        return categoryRepository.findAll(); // Trả về tất cả các category từ database
    }
    public List<Category> getAllCateories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id){
        return categoryRepository.findById(id);
    }

    public Category addCategory(Category product){
        categoryRepository.save(product);
        return product;
    }

    public Category updateCategory(int id, Category product){
        Category existingCategory = categoryRepository.findById(id);
        if (existingCategory != null){
            product.setId(id);
            categoryRepository.save(product);
            return product;
        }
        return null;
    }

    public void deleteCategory(int id){
        Category existingCategory = categoryRepository.findById(id);
        if (existingCategory != null){
            categoryRepository.deleteById(id);
        }
    }
}
