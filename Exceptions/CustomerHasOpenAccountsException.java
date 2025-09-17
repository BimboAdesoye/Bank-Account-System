package Exceptions;

public class CustomerHasOpenAccountsException extends RuntimeException {
    public CustomerHasOpenAccountsException(String message) {
        super(message);
    }
}
