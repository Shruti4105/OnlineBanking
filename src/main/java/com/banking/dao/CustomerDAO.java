package com.banking.dao;

import com.banking.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository
public class CustomerDAO {

    @Autowired
    private DataSource dataSource;

    public Customer createCustomer(Customer customer) throws Exception {
        String query = "INSERT INTO customers (user_id, name, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             
            pstmt.setInt(1, customer.getUserId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));
                    return customer;
                } else {
                    throw new Exception("Creating customer failed, no ID obtained.");
                }
            }
        }
    }

    public Customer findByUserId(int userId) throws Exception {
        String query = "SELECT * FROM customers WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(rs.getInt("customer_id"), 
                                        rs.getInt("user_id"),
                                        rs.getString("name"), 
                                        rs.getString("phone"), 
                                        rs.getString("email"));
                }
            }
        }
        return null;
    }
}
