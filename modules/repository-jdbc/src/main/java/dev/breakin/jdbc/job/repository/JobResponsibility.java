package dev.breakin.jdbc.job.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("job_responsibilities")
@Getter
@AllArgsConstructor
public class JobResponsibility {
    @Column("responsibility")
    private String responsibility;
}
