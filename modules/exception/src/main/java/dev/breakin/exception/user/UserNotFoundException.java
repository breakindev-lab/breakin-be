package dev.breakin.exception.user;

import dev.breakin.exception.BadRequestException;

public class UserNotFoundException  extends BadRequestException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
