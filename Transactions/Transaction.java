package Transactions;
import Accounts.BankAccount;
import Utils.generateRandomNumbers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private final String status;



    private String generateTransactionID() {
        return "#" + generateRandomNumbers.generateNumbers(6, usedTransactionIds);
    }

    public Transaction(TransactionType transactionType, double amount, String fromAccount, String toAccount, String status) {
        transactionID = generateTransactionID();
        this.createdAt = Instant.now();
        this.transactionType = transactionType;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public String getReadableTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
        return formatter.format(createdAt);
    }

    @Override
    public String toString() {
       switch (transactionType) {
           case DEPOSIT -> {
               return String.format("Deposit of $%.2f into account %s [ID: %s, Date: %s, Status: %s]",
                       amount, toAccount, transactionID, getReadableTime(), status);
           }
           case WITHDRAWAL -> {
               return String.format("Withdrawal of $%.2f from account %s [ID: %s, Date: %s, Status: %s]",
                       amount, fromAccount, transactionID, getReadableTime(), status);
           }
           case TRANSFER -> {
               return String.format("Transfer of $%.2f into account #%s from account #%s [ID: %s, Date: %s, Status: %s]",
                       amount, toAccount, fromAccount, transactionID, getReadableTime(), status);
           }
           default -> {
               return String.format("Transaction %s of $%.2f into account %s [ID: %s, Date: %s, Status: %s]",
                       transactionType, amount, toAccount, transactionID, getReadableTime(), status);
           }
       }
    }
}
