package dev.breakin.crawler.task.job.contentGenerator.contentGenerator.detail.explanation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.openai.base.AbstractSingleGptRunner;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobResponsibilitiesExtractor
        extends AbstractSingleGptRunner<JobResponsibilitiesExtractor.JobResponsibilitiesResult> {

    protected JobResponsibilitiesExtractor(ChatModel chatModel, ObjectMapper objectMapper) {
        super(chatModel, objectMapper, JobResponsibilitiesResult.class);
    }

    @Override
    protected String getSystemPrompt() {
        return PROMPT;
    }

    private static final String PROMPT = """
        From the following job posting, extract the concrete **responsibilities** this role will perform.
        Return **between 2 and 6 items**.

        Guidelines:
        - Use short, action-oriented phrases that **start with a verb** (e.g., "Automate service operations", "Develop Spring APIs").
        - Be specific; summarize tasks explicitly mentioned in the posting (avoid vague catch-alls).
        - Exclude culture notes, benefits, hiring process, and team introductions.
        - De-duplicate similar items and keep each bullet concise (no full sentences).
        - Do not invent information; extract only what's stated.

        Return **JSON only** in this exact shape:

        {
          "responsibilities": [
            "Develop and maintain iOS app features",
            "Refactor legacy code and improve architecture",
            "Implement consistent UX across iOS/Android"
          ]
        }
        """;

    public record JobResponsibilitiesResult(List<String> responsibilities) {}
}