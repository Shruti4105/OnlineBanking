package com.banking.model;

public class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 5000.00;

    public CurrentAccount(int accountId, int customerId, String accountNumber, double balance) {
        super(accountId, customerId, accountNumber, balance, "CURRENT");
    }

    @Override
    public String getAccountDetailsAndRules() {
        return "Current Account supports high volume transactions. Overdraft limit: $" + OVERDRAFT_LIMIT;
    }
}
