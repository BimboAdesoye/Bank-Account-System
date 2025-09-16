package Accounts;

import Exceptions.InvalidTransactionException;

public class CheckingAccount extends BankAccount {
    private final int overDraftLimit;  //Switch the data type to bigDecimal

    public CheckingAccount(String accountHolderName, int initialDeposit, int overDraftLimit) {
        super(accountHolderName, initialDeposit);
        this.overDraftLimit = overDraftLimit;
    }

    public void withdraw(double amount) throws InvalidTransactionException {
       if (amount <= 0) {
           throw new InvalidTransactionException("Invalid withdrawal amount.");
       }

       if(amount <= balance) {
           balance -= amount;
       }
       else {
           double shortfall = amount - balance;

           if(shortfall < overDraftLimit) {
              balance -= amount;
           }
           else {
              throw new InvalidTransactionException("Amount exceeds overdraft limit");
           }
       }
    }

    public String toString() {
        return accountNumber + " | " + accountHolderName + " | " + " Checking | balance: " + balance;
    }
}
