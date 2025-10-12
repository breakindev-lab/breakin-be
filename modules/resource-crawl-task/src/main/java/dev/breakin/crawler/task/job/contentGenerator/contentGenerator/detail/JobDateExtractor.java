package dev.breakin.crawler.task.job.contentGenerator.contentGenerator.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.openai.base.AbstractSingleGptRunner;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class JobDateExtractor
        extends AbstractSingleGptRunner<JobDateExtractor.JobDateInfo> {

    protected JobDateExtractor(ChatModel chatModel, ObjectMapper objectMapper) {
        super(chatModel, objectMapper, JobDateInfo.class);
    }

    @Override
    protected String getSystemPrompt() {
        return PROMPT;
    }

    private static final String PROMPT = """
        Read the following job posting and extract the **recruitment period** in the fields below.

        - `startedAt`: hiring start date (if explicitly stated). Use format **YYYY.MM.DD**.
        - `endedAt`: application deadline. Use **YYYY.MM.DD**. If not stated or if hiring is open-ended, set to `null`.
        - `isOpenEnded`: set to `true` if phrases like "rolling", "open until filled", "no deadline" appear. If a concrete deadline exists, set to `false`.

        Rules:
        - Do not guess. Only fill fields when the posting provides clear evidence.
        - If only a month/year is provided, do not fabricate a day; leave the date `null`.
        - Normalize localized date expressions to the required format when possible.

        Return **JSON only** in this exact shape:

        ```json
        {
          "startedAt": "2025.07.10",
          "endedAt": null,
          "isOpenEnded": true
        }
        ```
        """;

    public record JobDateInfo(
            String startedAt,
            String endedAt,
            boolean isOpenEnded
    ) {}
}
