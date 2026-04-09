package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.time.LocalDateTime;

@Service
public class BankingService {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    public void deposit(int accountId, double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Deposit amount must be strictly greater than zero.");
        }

        try (Connection conn = accountDAO.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try {
                Account account = accountDAO.findById(accountId);
                if (account == null) throw new Exception("Account not found.");

                double newBalance = account.getBalance() + amount;
                accountDAO.updateBalance(accountId, newBalance, conn);

                Transaction txn = new Transaction(0, accountId, "DEPOSIT", amount, null, LocalDateTime.now());
                transactionDAO.addTransaction(txn, conn);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void withdraw(int accountId, double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Withdrawal amount must be strictly greater than zero.");
        }

        try (Connection conn = accountDAO.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try {
                Account account = accountDAO.findById(accountId);
                if (account == null) throw new Exception("Account not found.");
                
                // Insufficient balance validation requested
                if (account.getBalance() < amount) {
                    throw new Exception("Insufficient balance for withdrawal.");
                }

                double newBalance = account.getBalance() - amount;
                accountDAO.updateBalance(accountId, newBalance, conn);

                Transaction txn = new Transaction(0, accountId, "WITHDRAW", amount, null, LocalDateTime.now());
                transactionDAO.addTransaction(txn, conn);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void transfer(int fromAccountId, String toAccountNumber, double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Transfer amount must be strictly greater than zero.");
        }

        try (Connection conn = accountDAO.getConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try {
                Account fromAccount = accountDAO.findById(fromAccountId);
                if (fromAccount == null) throw new Exception("Source account not found.");
                
                if (fromAccount.getBalance() < amount) {
                    throw new Exception("Insufficient balance for transfer.");
                }

                Account toAccount = accountDAO.findByAccountNumber(toAccountNumber);
                if (toAccount == null) {
                    throw new Exception("Target account number does not exist.");
                }
                
                if (fromAccountId == toAccount.getAccountId()) {
                    throw new Exception("Cannot transfer to the same account.");
                }

                // Update sender
                accountDAO.updateBalance(fromAccountId, fromAccount.getBalance() - amount, conn);
                // Update receiver
                accountDAO.updateBalance(toAccount.getAccountId(), toAccount.getBalance() + amount, conn);

                // Create transactions
                Transaction txnOut = new Transaction(0, fromAccountId, "TRANSFER_OUT", amount, toAccount.getAccountId(), LocalDateTime.now());
                Transaction txnIn = new Transaction(0, toAccount.getAccountId(), "TRANSFER_IN", amount, fromAccountId, LocalDateTime.now());
                
                transactionDAO.addTransaction(txnOut, conn);
                transactionDAO.addTransaction(txnIn, conn);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
