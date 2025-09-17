package Bank;
import Accounts.BankAccount;
import Accounts.CheckingAccount;
import Accounts.SavingsAccount;
import Customers.Customer;
import Exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    String name;
    private static final Map<String, BankAccount> accounts = new HashMap<>();
    private static final Map<String, Customer> customers = new HashMap<>();

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

    public void deposit(String accountNumber, double amount) throws AccountNotFoundException, InvalidTransactionException{
        BankAccount account = getAccount(accountNumber);
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException,
            InvalidTransactionException{
        BankAccount account = getAccount(accountNumber);
        if(account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        account.withdraw(amount);
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
}