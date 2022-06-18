package by.it.academy.account.scheduler.dao.entity;

import by.it.academy.account.scheduler.model.TimeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scheduled_operations", schema = "app")
public class ScheduledOperationEntity {

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
    private UUID account;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigDecimal value;
    @Column(nullable = false)
    private UUID currency;
    @Column(nullable = false)
    private UUID category;
    @Column(nullable = false)
    private String username;

}
