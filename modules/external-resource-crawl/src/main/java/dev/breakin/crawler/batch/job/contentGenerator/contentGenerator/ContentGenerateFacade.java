package dev.breakin.crawler.batch.job.contentGenerator.contentGenerator;

import dev.breakin.crawler.batch.job.contentGenerator.contentGenerator.detail.*;
import dev.breakin.crawler.batch.job.contentGenerator.contentGenerator.detail.explanation.*;
import dev.breakin.crawler.batch.job.contentGenerator.contentGenerator.detail.hiringProcess.*;
import dev.breakin.model.common.Company;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import dev.breakin.model.job.*;
import dev.breakin.openai.base.GptParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContentGenerateFacade {

    private final JobTechCategoryExtractor techCategoryExtractor;
    private final JobLocationExtractor locationExtractor;
    private final JobEmploymentTypeExtractor employmentTypeExtractor;
    private final JobPositionCategoryExtractor positionCategoryExtractor;
    private final JobRemotePolicyExtractor remotePolicyExtractor;
    private final JobOrganizationExtractor organizationExtractor;
    private final JobDateExtractor dateExtractor;
    private final JobHiringProcessExtractor hiringProcessExtractor;
    private final JobInterviewStepsExtractor interviewStepsExtractor;
    private final JobOneLineSummaryExtractor oneLineSummaryExtractor;
    private final JobPositionIntroductionExtractor positionIntroductionExtractor;
    private final JobPositionRequirementsExtractor positionRequirementsExtractor;
    private final JobResponsibilitiesExtractor responsibilitiesExtractor;
    private final JobRequiredExperienceExtractor requiredExperienceExtractor;

    public Job generate(String text, String url, String title, Company company) {
        // Basic info extraction
        var techCategoryResult = techCategoryExtractor.run(GptParams.ofMini(text));
        var locationResult = locationExtractor.run(GptParams.ofMini(text));
        var employmentTypeResult = employmentTypeExtractor.run(GptParams.ofMini(text));
        var positionCategoryResult = positionCategoryExtractor.run(GptParams.ofMini(text));
        var remotePolicyResult = remotePolicyExtractor.run(GptParams.ofMini(text));
        var organizationResult = organizationExtractor.run(GptParams.ofMini(text));
        var dateResult = dateExtractor.run(GptParams.ofMini(text));
        var oneLineSummaryResult = oneLineSummaryExtractor.run(GptParams.ofMini(text));

        // Hiring process extraction (two-step)
        var hiringProcessText = hiringProcessExtractor.run(GptParams.ofMini(text));
        var interviewStepsResult = interviewStepsExtractor.run(GptParams.ofMini(hiringProcessText.hiringProcess()));

        // Explanation extraction
        var positionIntroResult = positionIntroductionExtractor.run(GptParams.ofMini(text));
        var requirementsResult = positionRequirementsExtractor.run(GptParams.ofMini(text));
        var responsibilitiesResult = responsibilitiesExtractor.run(GptParams.ofMini(text));

        var requiredExperienceResult = requiredExperienceExtractor.run(GptParams.ofMini(text));

        // Convert tech categories
        List<TechCategory> techCategories = techCategoryResult.categories().stream()
                .map(TechCategory::safeFrom)
                .filter(cat -> cat != null)
                .collect(Collectors.toList());

        // Convert employment type
        EmploymentType employmentType = EmploymentType.from(employmentTypeResult.employmentType());

        // Convert position category
        PositionCategory positionCategory = PositionCategory.fromString(positionCategoryResult.positionCategory());

        // Convert remote policy
        RemotePolicy remotePolicy = RemotePolicy.fromString(remotePolicyResult.remotePolicy());

        // Parse dates
        Instant startedAt = parseDate(dateResult.startedAt());
        Instant endedAt = parseDate(dateResult.endedAt());


        // Build fullDescription
        StringBuilder fullDescBuilder = new StringBuilder();
        if (positionIntroResult.introduction() != null && !positionIntroResult.introduction().isEmpty()) {
            fullDescBuilder.append(positionIntroResult.introduction()).append("\n\n");
        }
        if (responsibilitiesResult.responsibilities() != null && !responsibilitiesResult.responsibilities().isEmpty()) {
            fullDescBuilder.append("## Responsibilities\n");
            responsibilitiesResult.responsibilities().forEach(r -> fullDescBuilder.append("- ").append(r).append("\n"));
            fullDescBuilder.append("\n");
        }
        if (requirementsResult.qualifications() != null && !requirementsResult.qualifications().isEmpty()) {
            fullDescBuilder.append("## Qualifications\n");
            requirementsResult.qualifications().forEach(q -> fullDescBuilder.append("- ").append(q).append("\n"));
            fullDescBuilder.append("\n");
        }
        if (requirementsResult.preferredQualifications() != null && !requirementsResult.preferredQualifications().isEmpty()) {
            fullDescBuilder.append("## Preferred Qualifications\n");
            requirementsResult.preferredQualifications().forEach(pq -> fullDescBuilder.append("- ").append(pq).append("\n"));
        }
        String fullDescription = fullDescBuilder.toString().trim();

        return new Job(
                null,
                url, // url - will be set by caller
                company, // company - will be set by caller
                title, // title - will be set by caller
                organizationResult.organization(),
                text, // markdownBody
                oneLineSummaryResult.oneLineSummary(),
                requiredExperienceResult.minYears(), // minYears
                requiredExperienceResult.maxYears(), // maxYears
                requiredExperienceResult.experienceRequired(), // experienceRequired
                CareerLevel.ENTRY, // careerLevel - default
                employmentType,
                positionCategory,
                remotePolicy,
                techCategories,
                startedAt,
                endedAt,
                dateResult.isOpenEnded(),
                false, // isClosed
                locationResult.locations(),
                positionIntroResult.introduction(),
                responsibilitiesResult.responsibilities(),
                requirementsResult.qualifications(),
                requirementsResult.preferredQualifications(),
                fullDescription,
                interviewStepsResult.hasAssignment(),
                interviewStepsResult.hasCodingTest(),
                interviewStepsResult.hasLiveCoding(),
                interviewStepsResult.interviewCount(),
                interviewStepsResult.interviewDays(),
                Popularity.empty(), // popularity - will be initialized
                false, // isDeleted
                Instant.now(),
                Instant.now()
        );
    }

    private Instant parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            log.warn("Failed to parse date: {}", dateStr, e);
            return null;
        }
    }
}
