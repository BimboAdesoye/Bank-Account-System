package Bank;
import Accounts.BankAccount;
import Accounts.CheckingAccount;
import Accounts.SavingsAccount;
import Exceptions.AccountNotFoundException;
import Exceptions.InsufficientFundsException;
import Exceptions.InvalidTransactionException;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    String name;
    private static Map<String, BankAccount> accounts = new HashMap<>();

    public Bank(String name) {
        this.name = name;
    }

    public String openAccount(String accountHolderName, int initialDeposit) {
        SavingsAccount savingsAccount = new SavingsAccount(accountHolderName, initialDeposit);
        accounts.put(savingsAccount.getAccountNumber(), savingsAccount);
        return savingsAccount.getAccountNumber();
    }

    public String openAccount(String accountHolderName, int initialDeposit, int overDraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(accountHolderName, initialDeposit, overDraftLimit);
        accounts.put(checkingAccount.getAccountNumber(), checkingAccount);
        return checkingAccount.getAccountNumber();
    }

    public BankAccount getAccount(String accountNumber) {
       return accounts.get(accountNumber);
    }

    public void deposit(String accountNumber, double amount) throws AccountNotFoundException{
        BankAccount account = getAccount(accountNumber);
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }

        try {
            account.deposit(amount);
            System.out.println("You've successfully deposited $" + amount + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    public void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException,
            InvalidTransactionException{
        BankAccount account = getAccount(accountNumber);
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        account.withdraw(amount);
    }

    public void transfer(String fromAccount, String toAccount, double amount) throws AccountNotFoundException{
        BankAccount sendingAccount = getAccount(fromAccount);
        BankAccount receivingAccount = getAccount(toAccount);

        if(sendingAccount == null || receivingAccount == null) {
            throw new AccountNotFoundException("One of the accounts does not exist");
        }

        if(amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        try {
            sendingAccount.withdraw(amount);
            receivingAccount.deposit(amount);

            System.out.println("Transfer of $" + amount + " from " + sendingAccount.getAccountHolderName() + " to " +
                    receivingAccount.getAccountHolderName() + " completed.");
        }
        catch (InsufficientFundsException | InvalidTransactionException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println("Invalid argument: " + e.getMessage());
        }
    }

    public void listAccounts() {
        for(BankAccount account : accounts.values()) {
            System.out.println(account);
        }
    }

    public void closeAccount(String accountNumber) throws AccountNotFoundException{
        if(!accounts.containsKey(accountNumber)) {
           throw new AccountNotFoundException("No account found for number: " + accountNumber);
        }
        accounts.remove(accountNumber);
    }

    public void getTotalAssets() {
        double total = 0;
        for(BankAccount account : accounts.values()) {
            total += account.getBalance();
        }
        System.out.println("Total assets: $" + total);
    }
}
