package com.banking.dao;

import com.banking.model.Account;
import com.banking.model.CurrentAccount;
import com.banking.model.SavingsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAO {

    @Autowired
    private DataSource dataSource;

    public Account createAccount(Account account) throws Exception {
        String query = "INSERT INTO accounts (customer_id, account_number, balance, account_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             
            pstmt.setInt(1, account.getCustomerId());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setDouble(3, account.getBalance());
            pstmt.setString(4, account.getAccountType());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccountId(generatedKeys.getInt(1));
                    return account;
                } else {
                    throw new Exception("Creating account failed, no ID obtained.");
                }
            }
        }
    }

    public Account findByAccountNumber(String accNumber) throws Exception {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, accNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToAccount(rs);
                }
            }
        }
        return null;
    }

    public Account findById(int id) throws Exception {
        String query = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToAccount(rs);
                }
            }
        }
        return null;
    }

    public List<Account> findByCustomerId(int customerId) throws Exception {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE customer_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapRowToAccount(rs));
                }
            }
        }
        return accounts;
    }

    public void updateBalance(int accountId, double newBalance, Connection conn) throws Exception {
        String query = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
        }
    }

    public Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }

    private Account mapRowToAccount(ResultSet rs) throws Exception {
        int id = rs.getInt("account_id");
        int cusId = rs.getInt("customer_id");
        String accNum = rs.getString("account_number");
        double bal = rs.getDouble("balance");
        String type = rs.getString("account_type");

        if ("SAVINGS".equals(type)) {
            return new SavingsAccount(id, cusId, accNum, bal);
        } else {
            return new CurrentAccount(id, cusId, accNum, bal);
        }
    }
}
