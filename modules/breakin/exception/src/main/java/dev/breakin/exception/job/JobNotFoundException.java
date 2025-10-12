package dev.breakin.exception.job;

import dev.breakin.exception.BadRequestException;

public class JobNotFoundException extends BadRequestException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
