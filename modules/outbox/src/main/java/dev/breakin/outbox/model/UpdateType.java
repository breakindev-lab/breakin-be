package dev.breakin.outbox.model;

/**
 * Type of update for outbox events
 */
public enum UpdateType {
    CREATED,
    UPDATED,
    POPULARITY_ONLY,
    DELETED
}
