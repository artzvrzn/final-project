package by.itacademy.mail.scheduler.dao.entity;

import by.itacademy.mail.scheduler.model.TimeUnit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "scheduled_mail_reports", schema = "app")
public class ScheduledMailReportEntity {

    @Id
    private UUID id;
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp(3)")
    private LocalDateTime created;
    @Column(nullable = false, columnDefinition = "timestamp(3)")
    private LocalDateTime updated;
    @Column(nullable = false, columnDefinition = "timestamp(3)")
    private LocalDateTime startTime;
    @Column(nullable = false, columnDefinition = "timestamp(3)")
    private LocalDateTime stopTime;
    @Column(nullable = false)
    private Integer interval;
    @Column(nullable = false)
    private TimeUnit timeUnit;
    @Column(nullable = false)
    private String receiver;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false, columnDefinition = "character varying")
    private Map<String, Object> params;
    @Column(nullable = false)
    private String username;
}
