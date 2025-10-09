package dev.breakin.exception.comment;

import dev.breakin.exception.BadRequestException;

public class WrongCommentException extends BadRequestException {
    public WrongCommentException(String message) {
        super(message);
    }
}
