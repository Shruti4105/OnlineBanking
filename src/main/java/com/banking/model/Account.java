package com.banking.model;

public abstract class Account {
    private int accountId;
    private int customerId;
    private String accountNumber;
    private double balance;
    private String accountType;

    public Account(int accountId, int customerId, String accountNumber, double balance, String accountType) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    // Abstract method demonstrating OOP principles
    public abstract String getAccountDetailsAndRules();
}
