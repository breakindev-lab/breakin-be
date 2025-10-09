package dev.breakin.exception.communitypost;

import dev.breakin.exception.BadRequestException;

public class CommunityPostNotFoundException extends BadRequestException {

    public CommunityPostNotFoundException(String message) {
        super(message);
    }
}
