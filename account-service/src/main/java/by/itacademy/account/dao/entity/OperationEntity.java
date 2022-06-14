package by.itacademy.account.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "operations", schema = "app")
public class OperationEntity extends BaseEntity {

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private AccountEntity account;
    @Column(nullable = false)
    private UUID category;
    @Column(nullable = false)
    private BigDecimal value;
    @Column(nullable = false)
    private UUID currency;

}
