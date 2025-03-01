package Theoffice.Exception;

public class NotAuthorizedUserException extends Exception {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}