package dev.breakin.exception.techblog;

import dev.breakin.exception.BadRequestException;

public class TechBlogNotFoundException extends BadRequestException {
    public TechBlogNotFoundException(String message) {
        super(message);
    }
}
