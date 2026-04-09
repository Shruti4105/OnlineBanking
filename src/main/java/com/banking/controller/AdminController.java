package com.banking.controller;

import com.banking.dao.TransactionDAO;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TransactionDAO transactionDAO;

    // Helper method to check role
    private boolean checkRole(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!checkRole(session)) return "redirect:/login";

        try {
            List<User> users = adminService.getAllUsers();
            model.addAttribute("users", users);
            return "admin_dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading users: " + e.getMessage());
            return "admin_dashboard";
        }
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session, Model model) {
        if (!checkRole(session)) return "redirect:/login";

        try {
            adminService.deleteUser(id);
            return "redirect:/admin/dashboard?success=UserDeleted";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete user: " + e.getMessage());
            return "admin_dashboard";
        }
    }

    @GetMapping("/report")
    public String report(HttpSession session, Model model) {
        if (!checkRole(session)) return "redirect:/login";

        try {
            List<Transaction> allTxns = transactionDAO.getAllTransactions();
            model.addAttribute("transactions", allTxns);
            return "report";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load transactions: " + e.getMessage());
            return "report";
        }
    }
}
