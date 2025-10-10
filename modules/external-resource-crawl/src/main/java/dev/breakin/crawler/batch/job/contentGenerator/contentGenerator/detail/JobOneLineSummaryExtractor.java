package dev.breakin.crawler.batch.job.contentGenerator.contentGenerator.detail;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.openai.base.AbstractSingleGptRunner;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class JobOneLineSummaryExtractor
        extends AbstractSingleGptRunner<JobOneLineSummaryExtractor.JobOneLineSummaryResult> {

    protected JobOneLineSummaryExtractor(ChatModel chatModel, ObjectMapper objectMapper) {
        super(chatModel, objectMapper, JobOneLineSummaryResult.class);
    }

    @Override
    protected String getSystemPrompt() {
        return PROMPT;
    }

    private static final String PROMPT = """
            Read the job posting below and produce a **single-line** summary of the core work or project
            (keep it concise, ideally within 8–15 words).
            
            Rules:
            - Do NOT include company, organization, or team names.
            - If a concrete project name exists, returning just that project name is acceptable.
              - e.g., "Self-service automation for customer support"
            - If responsibilities are clear, write one sentence capturing the most important duty.
              - e.g., "Build a self-service automation system using customer inquiry data"
            - Avoid specific service brand names; use generic wording (e.g., avoid “within FooApp”).
            - Avoid overly generic phrases; the line should reveal the real work.
              - ❌ "Server development", "Backend development"
              - ✅ "Develop CS systems for post-order customer experience"
            
            Output **JSON only** in the following shape:
            
            ```json
            {
              "oneLineSummary": "Design and operate backend for high-traffic personalized services"
            }```
            """;

    public record JobOneLineSummaryResult(String oneLineSummary) {
    }
}
