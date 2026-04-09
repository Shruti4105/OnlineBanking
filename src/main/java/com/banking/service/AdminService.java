package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.CustomerDAO;
import com.banking.dao.TransactionDAO;
import com.banking.dao.UserDAO;
import com.banking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AccountDAO accountDAO;
    
    @Autowired
    private TransactionDAO transactionDAO;

    public List<User> getAllUsers() throws Exception {
        return userDAO.findAll();
    }
    
    public void deleteUser(int userId) throws Exception {
        userDAO.deleteUser(userId);
    }
}
