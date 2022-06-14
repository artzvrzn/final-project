package by.itacademy.account.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "balances", schema = "app")
public class BalanceEntity {

    @Id
    @Column(name = "account_id", updatable = false)
    private UUID id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id", updatable = false)
    private AccountEntity account;
    @Column(nullable = false)
    private BigDecimal value;
    @Column(nullable = false, updatable = false)
    private UUID currency;

}
