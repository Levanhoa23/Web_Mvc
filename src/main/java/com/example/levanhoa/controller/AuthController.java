package com.example.levanhoa.controller;

import com.example.levanhoa.model.User;
import com.example.levanhoa.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "Website/login";
    }

    @PostMapping("/login")
    public String processLogin(User user, HttpServletRequest request, HttpSession session, Model model) {
        User registeredUser = userService.getUserByEmail(user.getEmail());

        if (registeredUser != null && user.getPassword().equals(registeredUser.getPassword())) {
            session.setAttribute("user", registeredUser); // Lưu user vào session

            // Kiểm tra role để điều hướng
            if ("admin".equals(registeredUser.getRole())) {
                return "redirect:/Admin/dashboard"; // Chuyển hướng admin đến dashboard
            }

            // Kiểm tra URL cần quay lại
            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            session.removeAttribute("redirectAfterLogin"); // Xóa session để tránh lỗi

            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/cart";
        } else {
            model.addAttribute("message", "Email hoặc mật khẩu không đúng!");
            model.addAttribute("messageColor", "red");
            return "Website/login";
        }
    }

    @GetMapping("register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "Website/register";
    }

    @PostMapping("register")
    public String processRegister(User user, Model model) {
        if (userService.getUserByEmail(user.getEmail()) != null) {
            model.addAttribute("message", "Email đã tồn tại!");
            model.addAttribute("messageColor", "red");
            model.addAttribute("user", user);
            return "Website/register";
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }
        userService.addUser(user);

        model.addAttribute("message", "Đăng ký thành công! Bạn có thể đăng nhập ngay.");
        model.addAttribute("messageColor", "green");
        model.addAttribute("user", new User());

        return "Website/register";
    }
}
