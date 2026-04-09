package com.banking.model;

import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String type; // DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
    private double amount;
    private Integer targetAccountId;
    private LocalDateTime date;
    
    // Additional fields for display join
    private String accountNumber;
    private String targetAccountNumber;
    private String customerName;

    public Transaction() {}

    public Transaction(int transactionId, int accountId, String type, double amount, Integer targetAccountId, LocalDateTime date) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.targetAccountId = targetAccountId;
        this.date = date;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Integer getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(Integer targetAccountId) { this.targetAccountId = targetAccountId; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
    public void setTargetAccountNumber(String targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
