package dev.breakin.exception.reaction;

import dev.breakin.exception.BadRequestException;

public class ReactionNotFoundException extends BadRequestException {
    public ReactionNotFoundException(String message) {
        super(message);
    }
}
