package Accounts;

import Exceptions.InsufficientFundsException;
import Exceptions.InvalidTransactionException;
import Utils.generateRandomNumbers;

import java.util.HashSet;
import java.util.Set;

public abstract class BankAccount {
    private static final Set<String> usedAccountNumbers = new HashSet<>();
    protected final String accountNumber;
    protected final String accountHolderName;
    protected double balance;  //Switch the data type to bigDecimal

//    private String generateAccountNumber() {
//       String number;
//       do{
//           StringBuilder sb = new StringBuilder();
//           for(int i = 0; i < 10; i++) {
//               sb.append(random.nextInt(10));
//           }
//           number = sb.toString();
//       } while(usedAccountNumbers.contains(number));
//
//       usedAccountNumbers.add(number);
//       return number;
//    }

    private String generateAccountNumber() {
       return generateRandomNumbers.generateNumbers(10, usedAccountNumbers);
    }

    BankAccount(String accountHolderName, int initialDeposit) {
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        accountNumber = generateAccountNumber();
    }

    public String getAccountNumber() {
      return accountNumber;
    }

    public void deposit(double amount) throws InvalidTransactionException{
        if(amount <= 0) {
            throw new InvalidTransactionException("Amount must be positive");
        }
        balance += amount;
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException, InvalidTransactionException;

    public double getBalance() {
        return balance;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
}
