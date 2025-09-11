import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class BankAccount {
    private static final Set<String> usedAccountNumbers = new HashSet<>();
    protected final String accountNumber;
    protected final String accountHolderName;
    protected double balance;  //Switch the data type to bigDecimal

    static Random random = new Random();

    private String generateAccountNumber() {
       String number;
       do{
           StringBuilder sb = new StringBuilder();
           for(int i = 0; i < 10; i++) {
               sb.append(random.nextInt(10));
           }
           number = sb.toString();
       } while(usedAccountNumbers.contains(number));

       usedAccountNumbers.add(number);
       return number;
    }

    BankAccount(String accountHolderName, int initialDeposit) {
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        accountNumber = generateAccountNumber();
    }

    public String getAccountNumber() {
      return accountNumber;
    }

    public boolean deposit(int amount) {
        if(amount > 0) {
            balance += amount;
            return true;   // success
        }
            return false;   // failure
    }

    public abstract boolean withdraw(int amount);

    public double getBalance() {
        return balance;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
}
