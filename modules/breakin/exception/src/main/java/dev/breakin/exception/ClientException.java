package dev.breakin.exception;

public abstract class ClientException extends ApplicationException {
    protected ClientException(String status, String message) {
        super(status, message);
    }
}
