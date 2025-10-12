package dev.breakin.sync.es;

import dev.breakin.elasticsearch.api.job.JobIndexer;
import dev.breakin.elasticsearch.document.JobDoc;
import dev.breakin.elasticsearch.mapper.JobDocMapper;
import dev.breakin.infra.job.repository.JobRepository;
import dev.breakin.model.common.TargetType;
import dev.breakin.model.job.Job;
import dev.breakin.model.job.JobIdentity;
import dev.breakin.outbox.command.FindPendingEventsCommand;
import dev.breakin.outbox.model.OutboxEvent;
import dev.breakin.outbox.reader.OutboxEventReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobEsSyncTask {

    private final JobIndexer jobIndexer;
    private final JobRepository jobRepository;
    private final OutboxEventReader outboxEventReader;
    private final JobDocMapper jobDocMapper;

    public void run() {
        log.info("Starting Job ES sync task");

        // 1. 미처리 이벤트 조회 (JOB 타입만, 최대 100개)
        List<OutboxEvent> events = outboxEventReader.findPending(
                FindPendingEventsCommand.ofType(100, TargetType.JOB)
        );

        if (events.isEmpty()) {
            log.debug("No pending events to process");
            return;
        }

        log.info("Found {} pending events", events.size());

        // 2. 각 이벤트를 하나씩 처리 & 상태 업데이트
        int successCount = 0;
        int failCount = 0;

        for (OutboxEvent event : events) {
            try {
                processEvent(event);
                outboxEventReader.update(event.markAsCompleted());
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process event: eventId={}, error={}",
                        event.getId(), e.getMessage(), e);
                outboxEventReader.update(event.markAsFailed(e.getMessage()));
                failCount++;
            }
        }

        log.info("Job ES sync completed: success={}, failed={}", successCount, failCount);
    }

    private void processEvent(OutboxEvent event) {
        // Job 조회
        Job job = jobRepository.findById(new JobIdentity(event.getTargetId()))
                .orElseThrow(() -> new IllegalStateException("Job not found: " + event.getTargetId()));

        // JobDoc 변환
        JobDoc jobDoc = jobDocMapper.toDoc(job);

        // ES 인덱싱
        jobIndexer.indexOne(jobDoc);

        log.debug("Successfully indexed job: jobId={}", job.getJobId());
    }
}
