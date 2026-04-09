package com.banking.dao;

import com.banking.model.User;
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
public class UserDAO {
    
    @Autowired
    private DataSource dataSource;

    public User createUser(User user) throws Exception {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                    return user;
                } else {
                    throw new Exception("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    public User findByUsername(String username) throws Exception {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), 
                                  rs.getString("username"), 
                                  rs.getString("password"), 
                                  rs.getString("role"));
                }
            }
        }
        return null;
    }

    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role != 'ADMIN'"; // don't list admin normally realistically, but we can return all
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users");
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                users.add(new User(rs.getInt("user_id"), 
                                   rs.getString("username"), 
                                   rs.getString("password"), 
                                   rs.getString("role")));
            }
        }
        return users;
    }
    
    public void deleteUser(int userId) throws Exception {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
}
