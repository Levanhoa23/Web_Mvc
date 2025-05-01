package com.example.levanhoa.controller;

import com.example.levanhoa.model.*;
import com.example.levanhoa.repository.OrderItemRepository;
import com.example.levanhoa.repository.OrderRepository;
import com.example.levanhoa.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductVariantService productVariantService; // Thêm dòng này
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    // Phương thức tạo mã đơn hàng ngẫu nhiên
    public String generateOrderCode() {
        return UUID.randomUUID().toString();  // Tạo mã đơn hàng ngẫu nhiên
    }

    @Autowired
    private EmailService emailService;
//guier email
    public void sendOrderConfirmationEmail(String userEmail, List<Product> orderCartItems, double subtotal, double shippingFee, double vat, double total) {
        String subject = "Order Confirmation";
        StringBuilder text = new StringBuilder();

        text.append("Dear Customer,\n\n");
        text.append("Your order has been successfully placed!\n\n");
        text.append("Order Details:\n");

        // Lấy thông tin sản phẩm từ giỏ hàng
        for (Product product : orderCartItems) {
            text.append("Product: " + product.getName() + "\n");
            text.append("Price: " + product.getPrice() + "\n");
            text.append("Quantity: " + product.getQuantity() + "\n\n");
        }

        // Thêm thông tin thanh toán
        text.append("Subtotal: " + subtotal + "\n");
        text.append("Shipping Fee: " + shippingFee + "\n");
        text.append("VAT: " + vat + "\n");
        text.append("Total: " + total + "\n\n");

        text.append("Thank you for shopping with us!");

        // Gửi email
        emailService.sendEmail(userEmail, subject, text.toString());
    }

    @GetMapping
    public String home(Model model) {
        List<Product> allProducts = productService.getAllProducts();
        List<Product> featuredProducts = allProducts.size() > 8 ? allProducts.subList(0, 8) : allProducts;
        loadCategoryList(model);
        model.addAttribute("featuredProducts", featuredProducts);
        return "Website/index";
    }
    @GetMapping("/shop")
    public String shop(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "minPrice", required = false, defaultValue = "10") Double minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "1000") Double maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            Model model
    ) {
        Page<Product> productPage = productService.searchAndFilterProducts(keyword, minPrice, maxPrice, page, size);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "Website/shop";
    }
    @ModelAttribute("cartSize")
    public int cartSize() {
        return cartService.getCartSize();
    }
    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<Product> cartItems = cartService.getCartItems();
        double totalPrice = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("subtotal", cartService.getCartSubtotal());
        model.addAttribute("shippingFee", 0);
        model.addAttribute("vat", 19);
        model.addAttribute("total", cartService.getCartTotal(0, 19));
        return "Website/cart";
    }
    // Thêm endpoint POST để xử lý form với số lượng
    @PostMapping("cart/add/{id}")
    public String addToCartWithQuantity(@PathVariable("id") int id, @RequestParam("quantity") int quantity) {
        Product product = productService.getProductById(id);
        if (product != null) {
            cartService.addProductToCart(product, quantity);
        }
        return "redirect:/cart";
    }
    @PostMapping("cart/update/{id}")
    public String updateCart(@PathVariable("id") int id, @RequestParam("quantity") int quantity) {
        cartService.updateProductQuantity(id, quantity);

        return "redirect:/cart"; // ✅ Reload lại giỏ hàng để cập nhật subtotal
    }

    @GetMapping("cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") int id) {
        cartService.removeProductFromCart(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update-shipping")
    public String updateShipping(@RequestParam("shippingFee") double shippingFee, Model model) {
        double vat = 19; // VAT cố định
        double subtotal = cartService.getCartSubtotal();
        double total = cartService.getCartTotal(shippingFee, vat);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("vat", vat);
        model.addAttribute("total", total);
        return "cart"; // Reload trang giỏ hàng với giá trị mới
    }

    //hien thi thong tin khi nguoi dung truy cap can dau tien
    @GetMapping("/checkout")
    public String checkoutPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // Kiểm tra đăng nhập
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            session.setAttribute("redirectAfterLogin", "/checkout");
            return "redirect:/login";
        }

        // Lấy giỏ hàng
        List<Product> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/";
        }

        // Lấy thông tin khách hàng
        String name = (String) session.getAttribute("customerName");
        String phone = (String) session.getAttribute("customerPhone");
        String locality = (String) session.getAttribute("customerLocality");

        // Tính tổng tiền
        double subtotal = cartService.getCartSubtotal();
        double shippingFee = 0;  // Free shipping
        double vatRate = 0.19;
        double vat = subtotal * vatRate;
        double total = subtotal + shippingFee + vat;

        // Thêm vào model
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("customerName", name);
        model.addAttribute("customerPhone", phone);
        model.addAttribute("customerLocality", locality);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("vat", vat);
        model.addAttribute("total", total);

        return "Website/checkout";
    }

    //THong tin tư form ten, sdt, dia chi se luu về dây
    @PostMapping("/checkout")
    public String checkoutSubmit(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();

        // Kiểm tra đăng nhập
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            session.setAttribute("redirectAfterLogin", "/checkout"); // Lưu trang checkout để quay lại sau khi đăng nhập
            return "redirect:/login";
        }

        // Lấy thông tin khách hàng từ form
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String locality = request.getParameter("locality");

        // Lưu thông tin khách hàng vào session
        session.setAttribute("customerName", name);
        session.setAttribute("customerPhone", phone);
        session.setAttribute("customerLocality", locality);

        // Tạo mã đơn hàng
        String orderCode = generateOrderCode();  // Gọi phương thức tạo mã đơn hàng

        // Lấy giỏ hàng
        List<Product> cartItems = cartService.getCartItems();  // Lấy giỏ hàng từ cartService

        // Kiểm tra xem giỏ hàng có sản phẩm hay không
        if (cartItems == null || cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn hiện tại không có sản phẩm.");
            return "redirect:/cart";  // Quay lại trang giỏ hàng nếu giỏ hàng trống
        }

        // Tính toán tổng đơn hàng
        double subtotal = cartService.getCartSubtotal();
        double shippingFee = 0;  // Phí vận chuyển miễn phí
        double vatRate = 0.19;
        double vat = subtotal * vatRate;
        double total = subtotal + shippingFee + vat;

        // Tạo và lưu đơn hàng
        Order order = new Order(orderCode, "pending", loggedInUser, subtotal, vat, total);  // Trạng thái đơn hàng mặc định là "pending"
        orderRepository.save(order);  // Lưu đơn hàng vào cơ sở dữ liệu (đảm bảo đã inject đúng repository)

        // Lưu thông tin chi tiết đơn hàng vào bảng order_items
        for (Product product : cartItems) {
            OrderItem orderItem = new OrderItem(order, product, product.getQuantity(), product.getPrice());
            orderItemRepository.save(orderItem);  // Lưu chi tiết đơn hàng vào bảng order_items
        }

        // Lưu giỏ hàng vào session
        session.setAttribute("orderCartItems", cartItems);


        // Gửi thông tin giỏ hàng vào redirectAttributes
        redirectAttributes.addFlashAttribute("cartItems", cartItems);  // Thêm dòng này

        // Redirect đến trang xác nhận đơn hàng
        redirectAttributes.addFlashAttribute("subtotal", subtotal);
        redirectAttributes.addFlashAttribute("vat", vat);
        redirectAttributes.addFlashAttribute("total", total);

        return "redirect:/order-confirmation";  // Quay lại trang xác nhận đơn hàng
    }

    @GetMapping("/order-confirmation")
    public String orderConfirmation(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // Kiểm tra đăng nhập
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Kiểm tra xem thông tin giỏ hàng đã được lưu trong session chưa
        List<Product> orderCartItems = (List<Product>) session.getAttribute("orderCartItems");
        if (orderCartItems == null || orderCartItems.isEmpty()) {
            return "redirect:/";  // Chuyển về trang chủ nếu giỏ hàng rỗng
        }

        // Lấy thông tin khách hàng từ session
        String name = (String) session.getAttribute("customerName");
        String phone = (String) session.getAttribute("customerPhone");
        String locality = (String) session.getAttribute("customerLocality");

        // Kiểm tra nếu thông tin khách hàng không có trong session
        if (name == null || phone == null || locality == null) {
            return "redirect:/checkout"; // Quay lại trang checkout nếu thiếu thông tin
        }

        // Tính toán các giá trị đơn hàng
        double subtotal = cartService.getCartSubtotal();
        double shippingFee = 0;  // Free shipping
        double vatRate = 0.19;   // VAT 19%
        double vat = subtotal * vatRate;
        double total = subtotal + shippingFee + vat;

        // Thêm dữ liệu vào model để hiển thị
        model.addAttribute("cartItems", orderCartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("vat", vat);
        model.addAttribute("total", total);

        // Thêm thông tin khách hàng vào model để hiển thị trên trang order-confirmation
        model.addAttribute("customerName", name);
        model.addAttribute("customerPhone", phone);
        model.addAttribute("customerLocality", locality);
        model.addAttribute("user", loggedInUser);

        // Xóa giỏ hàng sau khi đơn hàng được xử lý
        cartService.clearCart();

        // Gửi email xác nhận đơn hàng
        sendOrderConfirmationEmail(loggedInUser.getEmail(), orderCartItems, subtotal, shippingFee, vat, total);

        return "Website/order-confirmation";
    }

    @GetMapping("about")
    public String about() {
        return "Website/about";
    }

    @GetMapping("products/{slug}-{id}.html")
    public String viewProduct(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        return "Website/product";
    }

    @GetMapping("contact")
    public String contact() {

        return "Website/contact";
    }

    @GetMapping("category/{slug}-{id}.html")
    public String viewCategory(@PathVariable("id") int id, Model model) {
        List<Product> productList = productService.getProductsByCategory(id);
        if (productList == null) {
            return "redirect:/";
        }
        model.addAttribute("productList", productList);
        return "Website/category";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Product> productList = productService.searchProducts(keyword);
        // Nếu không tìm thấy sản phẩm nào, quay về trang chủ
        if (productList.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("productList", productList);
        model.addAttribute("keyword", keyword); // Hiển thị lại keyword trên trang kết quả
        return "Website/search-results"; // Trang hiển thị kết quả tìm kiếm
    }

    //logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate(); // 📌 Xóa toàn bộ session
        return "redirect:/";  // 📌 Quay về trang chủ
    }
    // Trang My Account
    @GetMapping("my-account")
    public String myAccount(Model model) {
        return "Website/my-account";
    }
    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        List<ProductVariant> variants = productVariantService.findByProductId(id); // Lấy danh sách biến thể
        model.addAttribute("product", product);
        model.addAttribute("variants", variants); // Truyền danh sách biến thể sang giao diện
        return "product"; // Trả về trang Thymeleaf
    }
    private void loadCategoryList(Model model) {
        List<Category> categoryList = categoryService.getAllCateories();
        model.addAttribute("categoryList", categoryList);
    }
}