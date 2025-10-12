package dev.breakin.crawler.task.job.contentGenerator.contentGenerator.detail.explanation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.openai.base.AbstractSingleGptRunner;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class JobPositionIntroductionExtractor
        extends AbstractSingleGptRunner<JobPositionIntroductionExtractor.JobPositionIntroductionResult> {

    protected JobPositionIntroductionExtractor(ChatModel chatModel, ObjectMapper objectMapper) {
        super(chatModel, objectMapper, JobPositionIntroductionResult.class);
    }

    @Override
    protected String getSystemPrompt() {
        return PROMPT;
    }

    private static final String PROMPT = """
        From the following job posting, write a concise **two-sentence** summary that covers:
        1) which team/organization this role belongs to, and
        2) the primary mission/problems the role tackles.

        Rules:
        - Exclude HR greetings, benefits/perks, hiring process, and generic fluff.
        - Use natural, compact, international English.
        - Sentences must be complete and reflect specifics found in the posting.
        - Do not invent facts; summarize only what's stated.

        Return **JSON only** in this shape:

        ```json
        {
          "introduction": "..."
        }
        ```
        """;

    public record JobPositionIntroductionResult(String introduction) {}
}