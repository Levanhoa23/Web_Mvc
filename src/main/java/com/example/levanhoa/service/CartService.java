package com.example.levanhoa.service;

import com.example.levanhoa.model.Cart;
import com.example.levanhoa.model.Product;
import com.example.levanhoa.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final List<Product> cart = new ArrayList<>();

    public void addProductToCart(Product product, int quantity) {
        for (Product p : cart) {
            if (p.getId() == product.getId()) {
                p.setQuantity(p.getQuantity() + quantity); // ✅ Cộng dồn số lượng thay vì ghi đè
                return;
            }
        }
        // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm mới
        product.setQuantity(quantity);
        cart.add(product);
    }

    public void updateProductQuantity(int productId, int quantity) {
        for (Product p : cart) {
            if (p.getId() == productId) {
                p.setQuantity(quantity); // ✅ Cập nhật số lượng sản phẩm
                break;
            }
        }
    }

    @Autowired
    private CartRepository cartRepository;

    public List<Product> getCartItemsByUser(int userId) { // ❌ Dùng int thay vì Long
        return cartRepository.findByUserId(userId)
                .stream()
                .map(Cart::getProduct)
                .collect(Collectors.toList());
    }

    public void removeProductFromCart(int productId) {
        cart.removeIf(item -> item.getId() == productId);
    }
    public List<Product> getCartItems() {
        return cart;
    }
    public double getCartSubtotal() {
        return cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    public double getCartTotal(double shippingFee, double vat) {
        return getCartSubtotal() + shippingFee + vat;
    }

    public int getCartSize() {
        return cart.stream().mapToInt(Product::getQuantity).sum();
    }
    // Thêm phương thức clearCart để xóa toàn bộ giỏ hàng
    public void clearCart() {
        cart.clear();
    }


}
