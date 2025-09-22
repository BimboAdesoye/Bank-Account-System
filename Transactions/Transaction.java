package Transactions;
import Accounts.BankAccount;
import Utils.generateRandomNumbers;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Transaction {
    private static final Set<String> usedTransactionIds = new HashSet<>();
    private final String transactionID;
    private final TransactionType transactionType;
    private final double amount;
    private final String fromAccount;
    private final String toAccount;
    private Instant createdAt;



    private String generateTransactionID() {
        return "#" + generateRandomNumbers.generateNumbers(6, usedTransactionIds);
    }

    public Transaction(TransactionType transactionType, double amount, String fromAccount, String toAccount) {
        transactionID = generateTransactionID();
        this.createdAt = Instant.now();
        this.transactionType = transactionType;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getTransactionID() {
        return transactionID;
    }

    @Override
    public String toString() {
       switch (transactionType) {
           case DEPOSIT -> {
               return String.format("Deposit of $%.2f into account %s [ID: %s, Date: %s]",
                       amount, toAccount, transactionID, createdAt);
           }
           case WITHDRAWAL -> {
               return String.format("Withdrawal of $%.2f from account %s [ID: %s, Date: %s]",
                       amount, fromAccount, transactionID, createdAt);
           }
           case TRANSFER -> {
               return String.format("Transfer of $%.2f into account #%s from account #%s [ID: %s, Date: %s]",
                       amount, toAccount, fromAccount, transactionID, createdAt);
           }
           default -> {
               return String.format("Transaction %s of $%.2f into account %s [ID: %s, Date: %s]",
                       transactionType, amount, toAccount, transactionID, createdAt);
           }
       }
    }
}
