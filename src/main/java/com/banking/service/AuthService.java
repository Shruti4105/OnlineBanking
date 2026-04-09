package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.CustomerDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.SavingsAccount;
import com.banking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AccountDAO accountDAO;

    public User login(String username, String password) throws Exception {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void registerCustomer(String username, String password, String name, String phone, String email) throws Exception {
        // Check if user exists
        if (userDAO.findByUsername(username) != null) {
            throw new Exception("Username already exists!");
        }

        // Create User
        User user = new User(0, username, password, "CUSTOMER");
        user = userDAO.createUser(user);

        // Create Customer
        Customer customer = new Customer(0, user.getUserId(), name, phone, email);
        customer = customerDAO.createCustomer(customer);

        // Create Default Savings Account
        String accNumber = "SAV" + System.currentTimeMillis() % 100000 + customer.getCustomerId();
        Account account = new SavingsAccount(0, customer.getCustomerId(), accNumber, 0.0);
        accountDAO.createAccount(account);
    }
}
