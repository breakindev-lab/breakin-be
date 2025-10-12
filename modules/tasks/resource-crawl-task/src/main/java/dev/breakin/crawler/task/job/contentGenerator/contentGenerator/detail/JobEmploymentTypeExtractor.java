package dev.breakin.crawler.task.job.contentGenerator.contentGenerator.detail;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.model.job.EmploymentType;
import dev.breakin.openai.base.AbstractSingleGptRunner;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class JobEmploymentTypeExtractor
        extends AbstractSingleGptRunner<JobEmploymentTypeExtractor.JobEmploymentTypeResult> {

    protected JobEmploymentTypeExtractor(ChatModel chatModel, ObjectMapper objectMapper) {
        super(chatModel, objectMapper, JobEmploymentTypeResult.class);
    }

    @Override
    protected String getSystemPrompt() {
        return PROMPT;
    }

    public static final String PROMPT = """
        Read the following job posting and classify the **employment type** as exactly ONE of:

        - `FULL_TIME`: regular full-time employee (includes new grad/experienced permanent roles)
        - `CONTRACT`: fixed-term/contractor/freelance/project-based engagement
        - `INTERN`: internship (experience/convertible/hiring-track internships)
        - `UNKNOWN`: not clearly stated in the posting

        Do not infer. If unclear, use `UNKNOWN`.

        Return **JSON only** in this shape:

        ```json
        {
          "employmentType": "CONTRACT"
        }
        ```
        """;

    public record JobEmploymentTypeResult(String employmentType) {
        public EmploymentType getEmploymentTypeEnum() {
            return EmploymentType.from(employmentType);
        }
    }
}