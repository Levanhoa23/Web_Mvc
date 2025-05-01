package com.example.levanhoa.controller.dashboard_controller.product;

import com.example.levanhoa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/products")
public class DeleteProductController {
@Autowired
ProductService productService;
    @PostMapping("/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        // Xóa sản phẩm theo id
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
