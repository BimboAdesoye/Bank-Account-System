public class CheckingAccount extends BankAccount{
    private final int overDraftLimit;  //Switch the data type to bigDecimal

    CheckingAccount(String accountHolderName, int initialDeposit, int overDraftLimit) {
        super(accountHolderName, initialDeposit);
        this.overDraftLimit = overDraftLimit;
    }

    public boolean withdraw(int amount) {
       if (amount <= 0) {
           System.out.println("Invalid withdrawal amount.");
           return false;
       }

       if(amount <= balance) {
           balance -= amount;
           System.out.println("Successfully withdrawn " + amount + "$. New balance: " + balance + "$");
           return true;
       }

       int shortfall = (int) (amount - balance);

       if(shortfall < overDraftLimit) {
           balance -= amount;
           System.out.println("Withdrawn " + amount + "$ using overdraft. New balance: " + balance + "$");
           return true;
       }

        System.out.println("Withdrawal denied! Exceeds overdraft limit");
       return false;
    }
}
