public class SavingsAccount extends BankAccount{
    private static final double interestRate = 0.03;

    SavingsAccount(String accountHolderName, int initialDeposit) {
        super(accountHolderName, initialDeposit);
    }

    public boolean withdraw(int amount) {
        if(amount <= 0) {
            return false; // invalid withdrawal.negative amount
        }
        else if(amount > balance) {
            return false; // insufficient funds
        }
        else {
            balance -= amount;
            return true; // successful withdrawal
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
