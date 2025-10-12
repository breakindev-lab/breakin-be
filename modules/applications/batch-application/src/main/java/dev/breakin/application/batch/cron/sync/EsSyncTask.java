package dev.breakin.application.batch.cron.sync;

import dev.breakin.sync.task.task.es.JobEsSyncTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

import static dev.breakin.application.batch.ScheduleUtils.executeBatchTask;

@RequiredArgsConstructor
@Component
@Slf4j
public class EsSyncTask {

    private final JobEsSyncTask esSyncTask;
    private static final AtomicBoolean ES_SYNC_RUNNING = new AtomicBoolean(false);


    @Scheduled(cron = "*/3 * * * * ?")
    public void sync_job() {
        executeBatchTask(ES_SYNC_RUNNING, "es_sync_job", esSyncTask::run);
    }
}
