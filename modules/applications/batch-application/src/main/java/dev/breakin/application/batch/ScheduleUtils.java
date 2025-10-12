package dev.breakin.application.batch;


import dev.breakin.logging.LogContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ScheduleUtils {
    public static void setLogContext(String jobName) {
        LogContext.setBatchContext(jobName);
    }

    // ✅ 공통 배치 실행 함수 (중복 제거 + 실행 시간 측정 + 로깅 개선)
    public static void executeBatchTask(AtomicBoolean taskFlag, String taskName, Runnable task) {
        if (!taskFlag.compareAndSet(false, true)) {
            log.warn("{} 이미 실행 중 - 중복 실행 방지", taskName);
            return;
        }

        setLogContext(taskName);

        long startTime = System.currentTimeMillis();
        log.info("{} 실행 시작...", taskName);

        try {
            task.run(); // ✅ 실제 작업 실행
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("{} 완료! (실행 시간: {} ms)", taskName, elapsedTime);
        } catch (Exception e) {
            log.error("{} 실행 중 오류 발생: {}", taskName, e.getMessage(), e);
        } finally {
            taskFlag.set(false); // ✅ 작업 종료 후 플래그 해제
            LogContext.clear();
        }
    }
}
