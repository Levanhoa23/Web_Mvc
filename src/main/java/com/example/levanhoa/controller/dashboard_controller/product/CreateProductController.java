package com.example.levanhoa.controller.dashboard_controller.product;


import com.example.levanhoa.model.Category;
import com.example.levanhoa.model.Product;
import com.example.levanhoa.service.CategoryService;
import com.example.levanhoa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class CreateProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // hien thi form them san pham
    @GetMapping("/add-product")
    public String showAddProductForm(Model model){
        Product product =new Product();
        model.addAttribute("product", product);

        //Truyen danh sach cac category vao model
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        // tra ve view
        return "Admin/product/add-product";
    }

    //su ly khi submit form
    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("product") Product product){
        try{
            // Luu san pham vao co so du lieu
            productService.addProduct(product);
            // chuyen ve trang products
            return "redirect:/products";
        }catch (Exception e){
            e.printStackTrace();
            // quay lai form neu loi
            return "admin/product/add-product";
        }
    }
}
