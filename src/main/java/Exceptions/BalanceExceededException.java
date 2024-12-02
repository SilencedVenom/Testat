package Exceptions;

public class BalanceExceededException extends RuntimeException {
    public BalanceExceededException(String e) {
        super(e);
    }
}
