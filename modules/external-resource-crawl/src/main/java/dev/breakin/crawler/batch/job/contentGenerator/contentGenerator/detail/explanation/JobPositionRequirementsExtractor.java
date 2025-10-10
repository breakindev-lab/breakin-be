package dev.breakin.crawler.batch.job.contentGenerator.contentGenerator.detail.explanation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.openai.base.AbstractSingleGptRunner;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobPositionRequirementsExtractor
        extends AbstractSingleGptRunner<JobPositionRequirementsExtractor.JobPositionRequirementsResult> {

    public record JobPositionRequirementsResult(
            List<String> qualifications,
            List<String> preferredQualifications
    ) {}

    protected JobPositionRequirementsExtractor(ChatModel chatModel, ObjectMapper objectMapper) {
        super(chatModel, objectMapper, JobPositionRequirementsResult.class);
    }

    @Override
    protected String getSystemPrompt() {
        return PROMPT;
    }

    private static final String PROMPT = """
        From the following job posting, extract **Required Qualifications** and **Preferred Qualifications**.

        Rules:
        - **Required Qualifications** are must-have criteria to perform the role.
          - Examples: "3+ years backend experience", "Java & Spring proficiency", "CI/CD experience"
        - **Preferred Qualifications** are nice-to-have signals explicitly listed as preferences (e.g., “preferred,” “nice to have,” “plus”).
          - Examples: "MSA experience", "SwiftUI experience", "Open-source contributions"
        - If a criterion appears in both areas, **keep it in Required** and omit from Preferred.
        - Exclude company culture, team traits, benefits, and hiring process details.
        - Exclude **soft skills** (e.g., perseverance, passion, communication skills, self-driven, growth mindset).
        - Return **at most 6 items per list**.
        - De-duplicate items and keep each bullet **concise** (phrase form, not full sentences).
        - Do **not** invent information; extract only what is stated.

        Return **JSON only** in this exact shape:

        {
          "qualifications": [
            "Java and Spring backend development",
            "MySQL or PostgreSQL experience",
            "CI/CD pipeline operation"
          ],
          "preferredQualifications": [
            "MSA production experience",
            "Elasticsearch usage",
            "Open-source contributions"
          ]
        }
        """;
}