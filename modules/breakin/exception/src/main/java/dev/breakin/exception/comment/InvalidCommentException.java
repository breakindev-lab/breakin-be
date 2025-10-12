package dev.breakin.exception.comment;

import dev.breakin.exception.BadRequestException;

/**
 * Invalid comment exception
 *
 * Thrown when comment validation fails (invalid content, userId, etc.)
 */
public class InvalidCommentException extends BadRequestException {
    public InvalidCommentException(String message) {
        super(message);
    }
}
