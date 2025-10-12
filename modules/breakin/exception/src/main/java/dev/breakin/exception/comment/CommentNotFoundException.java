package dev.breakin.exception.comment;

import dev.breakin.exception.BadRequestException;

public class CommentNotFoundException extends BadRequestException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
