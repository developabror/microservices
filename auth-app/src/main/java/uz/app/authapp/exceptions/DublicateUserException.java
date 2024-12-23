package uz.app.authapp.exceptions;

public class DublicateUserException extends RuntimeException {
    public DublicateUserException(String message) {
        super(message);
    }
}
