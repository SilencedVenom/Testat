package Exceptions;

public class TransactionsNotFoundException extends RuntimeException {
    public TransactionsNotFoundException(String e) {
        super(e);
    }
}
