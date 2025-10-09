package dev.breakin.jdbc.job.repository;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.job.CareerLevel;
import dev.breakin.model.job.EmploymentType;
import dev.breakin.model.job.PositionCategory;
import dev.breakin.model.job.RemotePolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Set;

/**
 * Job Spring Data JDBC Entity
 *
 * Model 클래스 스펙을 기반으로 생성된 데이터베이스 매핑용 엔티티
 * - Popularity: @Embedded로 jobs 테이블에 컬럼으로 저장
 * - techCategories: 별도 테이블로 정규화
 */
@Table("jobs")
@Getter
@AllArgsConstructor
public class JobEntity {
    @Id
    private Long id;
    private String url;
    private String company;
    private String title;
    private String organization;
    private String markdownBody;
    private String oneLineSummary;
    private Integer minYears;
    private Integer maxYears;
    private Boolean experienceRequired;
    private CareerLevel careerLevel;
    private EmploymentType employmentType;
    private PositionCategory positionCategory;
    private RemotePolicy remotePolicy;

    // 1:N 관계 - job_tech_categories 테이블로 분리
    @MappedCollection(idColumn = "job_id", keyColumn = "job_id")
    private Set<JobTechCategory> techCategories;

    private Instant startedAt;
    private Instant endedAt;
    private Boolean isOpenEnded;
    private Boolean isClosed;
    private String location;
    private Boolean hasAssignment;
    private Boolean hasCodingTest;
    private Boolean hasLiveCoding;
    private Integer interviewCount;
    private Integer interviewDays;

    // Embedded - jobs 테이블에 컬럼으로 flatten
    @Embedded.Nullable
    private Popularity popularity;

    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;
}
