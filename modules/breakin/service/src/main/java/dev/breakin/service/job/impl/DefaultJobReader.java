package dev.breakin.service.job.impl;

import dev.breakin.exception.job.JobNotFoundException;
import dev.breakin.model.job.Job;
import dev.breakin.model.job.JobIdentity;
import dev.breakin.service.job.JobReader;
import dev.breakin.service.job.view.JobViewMemory;
import dev.breakin.infra.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Job 도메인 조회 서비스 구현체
 *
 * CQRS 패턴의 Query 책임을 구현하며,
 * Infrastructure Repository를 활용한 조회 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultJobReader implements JobReader {

    private final JobRepository jobRepository;
    private final JobViewMemory jobViewMemory;

    @Override
    public Job read(JobIdentity identity) {
        log.debug("Reading Job by id: {}", identity.getJobId());
        Job job = jobRepository.findById(identity)
                .orElseThrow(() -> new JobNotFoundException("Job with id " + identity.getJobId() + " not found"));


        // 조회수 증가 (비동기)
        jobViewMemory.countUp(job.getJobId());

        return job;
    }

    @Override
    public Job getById(JobIdentity identity) {
        log.debug("Fetching Job by id: {}", identity.getJobId());
        return jobRepository.findById(identity)
                .orElseThrow(() -> new JobNotFoundException("Job with id " + identity.getJobId() + " not found"));
    }

    @Override
    public List<Job> getAll() {
        log.debug("Fetching all Jobs");
        return jobRepository.findAll();
    }

    @Override
    public Job getByUrl(String url) {
        log.debug("Fetching Job by url: {}", url);
        return jobRepository.findByUrl(url)
                .orElseThrow(() -> new JobNotFoundException("URL: " + url));
    }

    @Override
    public List<Job> getByCompany(String company) {
        log.debug("Fetching Jobs by company: {}", company);
        return jobRepository.findByCompany(company);
    }
}
