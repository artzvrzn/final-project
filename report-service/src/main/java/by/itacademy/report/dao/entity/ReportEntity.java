package by.itacademy.report.dao.entity;

import by.itacademy.report.model.ReportStatus;
import by.itacademy.report.model.ReportType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reports", schema = "app")
public class ReportEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(nullable = false,updatable = false, columnDefinition = "timestamp(3)")
    private LocalDateTime created;
    @Column(nullable = false, columnDefinition = "timestamp(3)")
    private LocalDateTime updated;
    @Column(nullable = false)
    private ReportStatus status;
    @Column(nullable = false)
    private ReportType type;
    private String description;
    @Column(columnDefinition = "character varying")
    private Map<String, Object> params;
    @Column(nullable = false)
    private String username;
    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ReportFilePropertyEntity fileProperty;
}
