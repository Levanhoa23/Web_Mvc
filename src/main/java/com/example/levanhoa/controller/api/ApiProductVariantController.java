package com.example.levanhoa.controller.api;

import com.example.levanhoa.model.Product;
import com.example.levanhoa.service.ProductService;
import com.example.levanhoa.model.ProductVariant;
import com.example.levanhoa.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/product-variants")
public class ApiProductVariantController {
    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping("/same-product-different-color/{productId}/{color}")
    public List<ProductVariant> getVariantsWithDifferentColor(@PathVariable int productId, @PathVariable String color) {
        return productVariantService.findVariantsWithDifferentColor(productId, color);
    }
}
