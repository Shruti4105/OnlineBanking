package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.CustomerDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.service.BankingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private BankingService bankingService;

    // Helper method to check role
    private boolean checkRole(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"CUSTOMER".equals(user.getRole())) {
            return false;
        }
        return true;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";

        User user = (User) session.getAttribute("user");
        try {
            Customer cus = customerDAO.findByUserId(user.getUserId());
            List<Account> accounts = accountDAO.findByCustomerId(cus.getCustomerId());
            model.addAttribute("customer", cus);
            model.addAttribute("accounts", accounts);
            
            // Getting primary account transactions for display
            if (!accounts.isEmpty()) {
                Account primary = accounts.get(0);
                model.addAttribute("recentTransactions", transactionDAO.getTransactionsByAccountId(primary.getAccountId()));
            }

            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard " + e.getMessage());
            return "dashboard";
        }
    }

    @GetMapping("/deposit")
    public String depositPage(HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";
        return loadAccountOptions(session, model, "deposit");
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam int accountId, @RequestParam double amount, HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";
        try {
            bankingService.deposit(accountId, amount);
            model.addAttribute("success", "Deposited successfully!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return loadAccountOptions(session, model, "deposit");
    }

    @GetMapping("/withdraw")
    public String withdrawPage(HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";
        return loadAccountOptions(session, model, "withdraw");
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam int accountId, @RequestParam double amount, HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";
        try {
            bankingService.withdraw(accountId, amount);
            model.addAttribute("success", "Withdrawn successfully!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return loadAccountOptions(session, model, "withdraw");
    }

    @GetMapping("/transfer")
    public String transferPage(HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";
        return loadAccountOptions(session, model, "transfer");
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam int fromAccountId, @RequestParam String toAccountNumber, @RequestParam double amount, HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";
        try {
            bankingService.transfer(fromAccountId, toAccountNumber, amount);
            model.addAttribute("success", "Transfer successful!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return loadAccountOptions(session, model, "transfer");
    }

    @GetMapping("/transactions")
    public String transactionsPage(HttpSession session, Model model) {
        if (!checkRole(session, model)) return "redirect:/login";
        
        User user = (User) session.getAttribute("user");
        try {
            Customer cus = customerDAO.findByUserId(user.getUserId());
            List<Account> accounts = accountDAO.findByCustomerId(cus.getCustomerId());
            model.addAttribute("accounts", accounts);
            if (!accounts.isEmpty()) {
                // Return transactions for all accounts logic, for simplicity we fetch the first account
                 List<Transaction> txns = transactionDAO.getTransactionsByAccountId(accounts.get(0).getAccountId());
                 model.addAttribute("transactions", txns);
            }
            return "transactions";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading transactions");
            return "transactions";
        }
    }

    private String loadAccountOptions(HttpSession session, Model model, String viewName) {
        User user = (User) session.getAttribute("user");
        try {
            Customer cus = customerDAO.findByUserId(user.getUserId());
            List<Account> accounts = accountDAO.findByCustomerId(cus.getCustomerId());
            model.addAttribute("accounts", accounts);
            return viewName;
        } catch (Exception e) {
            model.addAttribute("error", "Error loading account data.");
            return viewName;
        }
    }
}
