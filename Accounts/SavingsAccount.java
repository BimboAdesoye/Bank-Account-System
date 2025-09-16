package Accounts;

import Exceptions.InsufficientFundsException;
import Exceptions.InvalidTransactionException;

public class SavingsAccount extends BankAccount {
    private static final double interestRate = 0.03;

    public SavingsAccount(String accountHolderName, int initialDeposit) {
        super(accountHolderName, initialDeposit);
    }

    public void withdraw(double amount) throws InsufficientFundsException, InvalidTransactionException {
        if(amount <= 0) {
            throw new InvalidTransactionException("Amount must be positive"); // invalid withdrawal.negative amount
        }
        else if(amount > balance) {
            throw new InsufficientFundsException("Insufficient funds"); // insufficient funds
        }
        else {
            balance -= amount;
        }
    }

    public double applyInterest() {
        double interest = balance * interestRate;
        System.out.println("Interest of " + interest + "$ has been added to your balance.");
        return balance + interest;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + accountHolderName + " | " + " Savings | balance: " + balance;
    }
}
