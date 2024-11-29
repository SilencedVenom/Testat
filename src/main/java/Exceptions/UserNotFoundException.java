package Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String e) {
        super(e);
    }
}
