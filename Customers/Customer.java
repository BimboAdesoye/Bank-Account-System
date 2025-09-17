package Customers;

import Exceptions.AccountNotFoundException;
import Utils.generateRandomNumbers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Customer {
    private static final Set<String> usedCustomerIds = new HashSet<>();
    private final String customerId;
    private final String name;
    private final String email;
    List<String> accountNumbers = new ArrayList<>();

    private String generateCustomerId() {
        return generateRandomNumbers.generateNumbers(6, usedCustomerIds);
    }

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        customerId = generateCustomerId();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void addAccountNumber(String accountNumber) {
        accountNumbers.add(accountNumber);
    }

    public void removeAccountNumber(String accountNumber) throws AccountNotFoundException{
        if(!accountNumbers.remove(accountNumber)) {
            throw new AccountNotFoundException("Account " + accountNumber + " not found for this customer.");
        }
    }

    public List<String> getAccountNumbers() {
       return accountNumbers;
    }

}
