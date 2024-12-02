package Exceptions;

public class NegativeTransactionException extends RuntimeException {
    public NegativeTransactionException(String e) {
        super(e);
    }
}
