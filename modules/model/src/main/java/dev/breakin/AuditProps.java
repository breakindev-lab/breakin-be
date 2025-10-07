package dev.breakin;

import java.time.Instant;

public interface AuditProps {
    Instant getCreatedAt();
    Instant getUpdatedAt();
}
