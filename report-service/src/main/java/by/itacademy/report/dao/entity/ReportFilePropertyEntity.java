package by.itacademy.report.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "report_properties", schema = "app")
public class ReportFilePropertyEntity {

    @Id
    @Column(name = "report_id", updatable = false)
    private UUID id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "report_id", updatable = false)
    private ReportEntity report;
    private String filename;
}
