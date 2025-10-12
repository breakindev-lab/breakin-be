package dev.breakin.outbox.recorder;

import dev.breakin.outbox.command.RecordOutboxEventCommand;
import dev.breakin.outbox.model.OutboxEvent;

/**
 * Interface for recording outbox events
 */
public interface OutboxEventRecorder {

    /**
     * Record a new outbox event
     *
     * @param command the command containing event details
     * @return the recorded event with generated ID
     */
    OutboxEvent record(RecordOutboxEventCommand command);
}
