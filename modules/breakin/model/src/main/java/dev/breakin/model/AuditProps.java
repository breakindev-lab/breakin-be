package dev.breakin.model;

import java.time.Instant;

public interface AuditProps {
    Instant getCreatedAt();
    Instant getUpdatedAt();
}
