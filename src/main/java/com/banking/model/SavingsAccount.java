package com.banking.model;

public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.04;

    public SavingsAccount(int accountId, int customerId, String accountNumber, double balance) {
        super(accountId, customerId, accountNumber, balance, "SAVINGS");
    }

    @Override
    public String getAccountDetailsAndRules() {
        return "Savings Account allows earning " + (INTEREST_RATE * 100) + "% interest. Withdrawals are subject to limits.";
    }
}
