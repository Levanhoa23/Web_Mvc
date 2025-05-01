package com.example.levanhoa.controller.dashboard_controller.product;


import com.example.levanhoa.model.Category;
import com.example.levanhoa.model.Product;
import com.example.levanhoa.repository.ProductRepository;
import com.example.levanhoa.service.CategoryService;
import com.example.levanhoa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class UpdateProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // Hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "Admin/product/edit-product";
    }

    // Xử lý form sau khi submit
    @PostMapping("/edit-product/{id}")
    public String updateProduct(@PathVariable("id") int id, @ModelAttribute("product") Product product) {
        try {
            product.setId(id);
            productService.addProduct(product);
            return "redirect:/products";
        } catch (Exception e) {
            e.printStackTrace();
            return "Admin/product/edit-product";
        }
    }
}
