package com.banking.controller;

import com.banking.model.User;
import com.banking.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/customer/dashboard";
            }
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = authService.login(username, password);
            if (user != null) {
                session.setAttribute("user", user);
                if ("ADMIN".equals(user.getRole())) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/customer/dashboard";
                }
            } else {
                model.addAttribute("error", "Invalid username or password.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "System error: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, 
                           @RequestParam String name, @RequestParam String phone, 
                           @RequestParam String email, Model model) {
        // Simple password validation requested
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "register";
        }
        
        try {
            authService.registerCustomer(username, password, name, phone, email);
            model.addAttribute("success", "Registration successful. Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
