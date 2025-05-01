package com.example.levanhoa.controller;

import com.example.levanhoa.model.User;
import com.example.levanhoa.repository.OrderRepository;
import com.example.levanhoa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/Admin")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    // Hiển thị trang admin
    @GetMapping
    public String showAdminPage(Model model) {
        // Bạn có thể muốn trả về thông tin thống kê ở đây
        long totalOrders = orderRepository.count();  // Tổng số đơn hàng
        double totalAmount = orderRepository.sumOrderAmount();  // Tổng tiền của tất cả đơn hàng
        long pendingOrders = orderRepository.countByStatus("pending");  // Đơn hàng đang chờ xử lý
        double pendingAmount = orderRepository.sumAmountByStatus("pending");  // Tổng tiền của đơn hàng đang chờ xử lý
        long deliveredOrders = orderRepository.countByStatus("delivered");  // Đơn hàng đã giao
        double deliveredAmount = orderRepository.sumAmountByStatus("delivered");  // Tổng tiền của đơn hàng đã giao
        long canceledOrders = orderRepository.countByStatus("canceled");  // Đơn hàng đã hủy
        double canceledAmount = orderRepository.sumAmountByStatus("canceled");  // Tổng tiền của đơn hàng đã hủy

        // Thêm dữ liệu vào model để hiển thị trên view
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("pendingAmount", pendingAmount);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("deliveredAmount", deliveredAmount);
        model.addAttribute("canceledOrders", canceledOrders);
        model.addAttribute("canceledAmount", canceledAmount);

        return "Admin/dashboard"; // Trả về view dashboard
    }

    // Hiển thị trang dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        System.out.println("== Đã vào được controller dashboard ==");

        // Lấy dữ liệu từ repository
        long totalOrders = orderRepository.count();
        double totalAmount = Optional.ofNullable(orderRepository.sumOrderAmount()).orElse(0.0); // Kiểm tra null và gán giá trị mặc định
        long pendingOrders = orderRepository.countByStatus("pending");
        double pendingAmount = Optional.ofNullable(orderRepository.sumAmountByStatus("pending")).orElse(0.0);
        long deliveredOrders = orderRepository.countByStatus("delivered");
        double deliveredAmount = Optional.ofNullable(orderRepository.sumAmountByStatus("delivered")).orElse(0.0);
        long canceledOrders = orderRepository.countByStatus("canceled");
        double canceledAmount = Optional.ofNullable(orderRepository.sumAmountByStatus("canceled")).orElse(0.0);

        // Thêm thông tin vào model
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("pendingAmount", pendingAmount);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("deliveredAmount", deliveredAmount);
        model.addAttribute("canceledOrders", canceledOrders);
        model.addAttribute("canceledAmount", canceledAmount);

        return "Admin/dashboard";
    }

    // Thêm user mới
    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user, Model model) {
        try {
            userService.addUser(user);
            model.addAttribute("message", "Thêm user thành công!");
            model.addAttribute("messageColor", "green");
        } catch (Exception e) {
            model.addAttribute("message", "Thêm user thất bại: " + e.getMessage());
            model.addAttribute("messageColor", "red");
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "Admin/dashboard";
    }

    // Hiển thị form chỉnh sửa user
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("message", "User không tồn tại!");
            model.addAttribute("messageColor", "red");
        } else {
            model.addAttribute("userToEdit", user);
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "Admin/dashboard";
    }

    // Cập nhật user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @ModelAttribute("userToEdit") User user, Model model) {
        try {
            userService.updateUser(id, user);
            model.addAttribute("message", "Cập nhật user thành công!");
            model.addAttribute("messageColor", "green");
        } catch (Exception e) {
            model.addAttribute("message", "Cập nhật user thất bại: " + e.getMessage());
            model.addAttribute("messageColor", "red");
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "Admin/dashboard";
    }

    // Xóa user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                model.addAttribute("message", "User không tồn tại!");
                model.addAttribute("messageColor", "red");
            } else {
                userService.deleteUser(id);
                model.addAttribute("message", "Xóa user thành công!");
                model.addAttribute("messageColor", "green");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Xóa user thất bại: " + e.getMessage());
            model.addAttribute("messageColor", "red");
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "Admin/dashboard";
    }
}
