package com.banking.dao;

import com.banking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAO {

    @Autowired
    private DataSource dataSource;

    public void addTransaction(Transaction txn, Connection conn) throws Exception {
        String query = "INSERT INTO transactions (account_id, type, amount, target_account_id, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, txn.getAccountId());
            pstmt.setString(2, txn.getType());
            pstmt.setDouble(3, txn.getAmount());
            if (txn.getTargetAccountId() != null) {
                pstmt.setInt(4, txn.getTargetAccountId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            pstmt.setTimestamp(5, Timestamp.valueOf(txn.getDate()));
            pstmt.executeUpdate();
        }
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, a.account_number " +
                       "FROM transactions t " +
                       "JOIN accounts a ON t.account_id = a.account_id " +
                       "WHERE t.account_id = ? OR t.target_account_id = ? " +
                       "ORDER BY t.date DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getInt("account_id"),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            (Integer) rs.getObject("target_account_id"),
                            rs.getTimestamp("date").toLocalDateTime()
                    );
                    t.setAccountNumber(rs.getString("account_number"));
                    transactions.add(t);
                }
            }
        }
        return transactions;
    }

    public List<Transaction> getAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, a.account_number, c.name as customer_name " +
                       "FROM transactions t " +
                       "JOIN accounts a ON t.account_id = a.account_id " +
                       "JOIN customers c ON a.customer_id = c.customer_id " +
                       "ORDER BY t.date DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        (Integer) rs.getObject("target_account_id"),
                        rs.getTimestamp("date").toLocalDateTime()
                );
                t.setAccountNumber(rs.getString("account_number"));
                t.setCustomerName(rs.getString("customer_name"));
                transactions.add(t);
            }
        }
        return transactions;
    }
}
