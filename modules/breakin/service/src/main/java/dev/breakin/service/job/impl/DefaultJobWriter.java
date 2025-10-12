package dev.breakin.service.job.impl;

import dev.breakin.model.common.TargetType;
import dev.breakin.model.job.Job;
import dev.breakin.model.job.JobIdentity;
import dev.breakin.outbox.command.RecordOutboxEventCommand;
import dev.breakin.outbox.recorder.OutboxEventRecorder;
import dev.breakin.service.job.JobWriter;
import dev.breakin.infra.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Job 도메인 변경 서비스 구현체
 *
 * CQRS 패턴의 Command 책임을 구현하며,
 * Infrastructure Repository를 활용한 변경 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultJobWriter implements JobWriter {

    private final JobRepository jobRepository;
    private final OutboxEventRecorder outboxEventRecorder;

    @Override
    public Job upsert(Job job) {
        log.info("Upserting Job: {}", job.getJobId());

        boolean isNewJob = job.getJobId() == null;

        // Job 저장
        Job saved = jobRepository.save(job);

        // Outbox 이벤트 기록
        RecordOutboxEventCommand command = isNewJob
            ? RecordOutboxEventCommand.created(TargetType.JOB, saved.getJobId())
            : RecordOutboxEventCommand.updated(TargetType.JOB, saved.getJobId());
        outboxEventRecorder.record(command);

        log.info("Job upserted successfully with outbox event: {}", saved.getJobId());
        return saved;
    }

    @Override
    public void delete(JobIdentity identity) {
        log.info("Deleting Job by id: {}", identity.getJobId());
        jobRepository.deleteById(identity);
        log.info("Job deleted successfully: {}", identity.getJobId());
    }
}
