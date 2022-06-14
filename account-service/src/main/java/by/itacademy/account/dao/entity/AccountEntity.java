package by.itacademy.account.dao.entity;

import by.itacademy.account.model.AccountType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "accounts", schema = "app")
public class AccountEntity extends BaseEntity {

    private String title;
    private String description;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BalanceEntity balance;
    private AccountType type;
}
