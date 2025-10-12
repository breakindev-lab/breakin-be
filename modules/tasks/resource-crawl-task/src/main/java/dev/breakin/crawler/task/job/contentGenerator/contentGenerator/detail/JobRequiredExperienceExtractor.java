package dev.breakin.crawler.task.job.contentGenerator.contentGenerator.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.openai.base.AbstractSingleGptRunner;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class JobRequiredExperienceExtractor
        extends AbstractSingleGptRunner<JobRequiredExperienceExtractor.JobRequiredExperienceResult> {

    protected JobRequiredExperienceExtractor(ChatModel chatModel, ObjectMapper objectMapper) {
        super(chatModel, objectMapper, JobRequiredExperienceResult.class);
    }

    @Override
    protected String getSystemPrompt() {
        return PROMPT;
    }

    private static final String PROMPT = """
        From the following job posting, extract **years of required experience**.

        Fields:
        - `minYears`: integer or null
        - `maxYears`: integer or null
        - `experienceRequired`: boolean — true if experience years are explicitly required; 
          false if the posting states “no experience required / experience not required / any experience / new grad welcome / 경력 무관 / 신입 가능”
          or if no explicit years are given.

        Normalization rules:
        - "X+ years", "at least X", "X years or more" → minYears = X, maxYears = null, experienceRequired = true
        - "X–Y years", "between X and Y" → minYears = X, maxYears = Y, experienceRequired = true
        - Single value like "X years experience" → minYears = X, maxYears = null, experienceRequired = true
        - Seniority words without numbers (e.g., "Senior", "Lead") do **not** imply numbers; if no explicit years, set experienceRequired = false
        - If unclear or not stated, set minYears = null, maxYears = null, experienceRequired = false
        - Do not guess. Extract only what is explicitly supported by the text.

        Return **JSON only** in this exact shape:

        {
          "minYears": 3,
          "maxYears": 6,
          "experienceRequired": true
        }
        """;

    public record JobRequiredExperienceResult(
            Integer minYears,
            Integer maxYears,
            Boolean experienceRequired
    ) {}
}
