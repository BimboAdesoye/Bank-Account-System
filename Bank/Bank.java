package Bank;
import Accounts.BankAccount;
import Accounts.CheckingAccount;
import Accounts.SavingsAccount;
import Customers.Customer;
import Exceptions.*;
import Transactions.Transaction;
import Transactions.TransactionType;

import java.util.*;

public class Bank {
    String name;
    private static final Map<String, BankAccount> accounts = new HashMap<>();
    private static final Map<String, Customer> customers = new HashMap<>();
    List<Transaction> transactions = new ArrayList<>();

    public Bank(String name) {
        this.name = name;
    }

    public String openAccount(String accountHolderName, int initialDeposit) {
        SavingsAccount savingsAccount = new SavingsAccount(accountHolderName, initialDeposit);
        accounts.put(savingsAccount.getAccountNumber(), savingsAccount);
        recordTransaction(TransactionType.DEPOSIT, initialDeposit, null, savingsAccount.getAccountNumber());
        return savingsAccount.getAccountNumber();
    }

    public String openAccount(String accountHolderName, int initialDeposit, int overDraftLimit) {
        CheckingAccount checkingAccount = new CheckingAccount(accountHolderName, initialDeposit, overDraftLimit);
        accounts.put(checkingAccount.getAccountNumber(), checkingAccount);
        recordTransaction(TransactionType.DEPOSIT, initialDeposit, null, checkingAccount.getAccountNumber());
        return checkingAccount.getAccountNumber();
    }

    public BankAccount getAccount(String accountNumber) {
       return accounts.get(accountNumber.trim().toUpperCase());
    }

    public void deposit(String accountNumber, double amount) throws AccountNotFoundException, InvalidTransactionException{
        BankAccount account = getAccount(accountNumber);
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        account.deposit(amount);
        recordTransaction(TransactionType.DEPOSIT, amount, null, accountNumber);
    }

    public void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException,
            InvalidTransactionException{
        BankAccount account = getAccount(accountNumber);
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        account.withdraw(amount);
        recordTransaction(TransactionType.WITHDRAWAL, amount, accountNumber, null);
    }

    public void transfer(String fromAccount, String toAccount, double amount) throws AccountNotFoundException,
            InvalidTransactionException, InsufficientFundsException{

        BankAccount sendingAccount = getAccount(fromAccount);
        BankAccount receivingAccount = getAccount(toAccount);

        if(sendingAccount == null) {
            throw new AccountNotFoundException("Sending Account not found");
        }

        if(receivingAccount == null) {
            throw new AccountNotFoundException("Receiving account not found");
        }
        sendingAccount.withdraw(amount);
        receivingAccount.deposit(amount);

        recordTransaction(TransactionType.TRANSFER, amount, fromAccount, toAccount);
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

    public String createCustomer(String name, String email) {
        Customer customer = new Customer(name, email);
        customers.put(customer.getCustomerId(), customer);
        return customer.getCustomerId();
    }

    public Customer getCustomer(String customerId) throws CustomerNotFoundException {
        Customer customer = customers.get(customerId);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        return customer;
    }

    public String openAccountForCustomer(String customerId, String accountType, int initialDeposit, int overDraftLimit)
            throws CustomerNotFoundException{
           Customer customer = getCustomer(customerId);

           String type = accountType.toLowerCase();
           String accountNumber = switch (type) {
               case "savings" -> openAccount(customer.getName(), initialDeposit);
               case "checking" -> openAccount(customer.getName(), initialDeposit, overDraftLimit);
               default -> throw new IllegalArgumentException("Invalid Account Type: " + accountType);
           };

           customer.addAccountNumber(accountNumber);

           return accountNumber;
    }

    public void closeAccountForCustomer(String accountNumber) throws AccountNotFoundException{
        Customer owner = null;

        for(Customer customer : customers.values()) {
            if (customer.getAccountNumbers().contains(accountNumber)) {
                owner = customer;
            }
        }

        if(owner == null) {
            throw new AccountNotFoundException("No customer linked to account: " + accountNumber);
        }

        closeAccount(accountNumber);
        owner.removeAccountNumber(accountNumber);
    }

    public void deleteCustomer(String customerId) throws CustomerNotFoundException, CustomerHasOpenAccountsException {
        Customer customer = getCustomer(customerId);

        if(!customer.getAccountNumbers().isEmpty()){
            throw new CustomerHasOpenAccountsException("Customer has open accounts. Please close them.");
        }

        customers.remove(customerId);
    }

    public List<BankAccount> listCustomerAccounts(String customerId) throws CustomerNotFoundException{
        Customer customer = getCustomer(customerId);

        List<String> customerAccountNumbers = customer.getAccountNumbers();

        List<BankAccount> customerAccounts = new ArrayList<>();

        for(String number : customerAccountNumbers) {
            BankAccount account = getAccount(number);
            if(account != null) {
            customerAccounts.add(account);
            }
        }

        return customerAccounts;
    }

    public void printCustomerAccounts(List<BankAccount> customerAccounts) {
        for (BankAccount account : customerAccounts) {
            System.out.println(account);
        }
    }

    public String recordTransaction(TransactionType transactionType, double amount, String fromAccount, String toAccount){
        Transaction transaction = new Transaction(transactionType, amount, fromAccount, toAccount);
        transactions.add(transaction);
        return transaction.getTransactionID();
    }

    public void listTransactions() {
        if(transactions.isEmpty()) {
            System.out.println("There are no transactions");
        }
        else {
            for(Transaction transaction : transactions) {
               System.out.println(transaction);
           }
        }
    }

    public List<Transaction> listTransactionsForAccount(String accountNumber){
        List<Transaction> accountTransactions = new ArrayList<>();

        for(Transaction transaction : transactions) {
            if(accountNumber.equals(transaction.getToAccount()) || accountNumber.equals(transaction.getFromAccount())) {
                 accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }

    public List<Transaction> listTransactionsForCustomers(String customerId) throws CustomerNotFoundException{
        Customer customer = getCustomer(customerId);
        List<String> accountNumbers = customer.getAccountNumbers();

        Set<Transaction> customerTransactions = new HashSet<>(); /* Set instead of List to prevent duplicates in case a transaction
                                                                    happens between two accounts owned by the same customer.*/

        for(Transaction transaction : transactions) {
                if(accountNumbers.contains(transaction.getToAccount()) || accountNumbers.contains(transaction.getFromAccount())) {
                    customerTransactions.add(transaction);
                }
            }

        if(customerTransactions.isEmpty()) {
            System.out.println("No transactions found for this customer");
        }

        return new ArrayList<>(customerTransactions);

    }

    public List<Transaction> listTransactionsByType(TransactionType transactionType) {
        List<Transaction> typeTransactions = new ArrayList<>();

        for(Transaction transaction : transactions) {
            if(transactionType == transaction.getTransactionType()) {
                typeTransactions.add(transaction);
            }
        }
        return typeTransactions;
    }

}