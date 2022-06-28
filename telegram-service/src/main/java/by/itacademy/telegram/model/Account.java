package by.itacademy.telegram.model;

import by.itacademy.telegram.model.constant.AccountType;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Account implements Stateful {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID uuid;
    @JsonProperty(value = "dt_updated", access = JsonProperty.Access.WRITE_ONLY)
    private long updated;
    private String title;
    private String description;
    private BigDecimal balance;
    private AccountType type;
    private UUID currency;
    @JsonIgnore
    private int state;
    @JsonIgnore
    private LocalDateTime instantiated = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equal(title, account.title)
                && Objects.equal(description, account.description)
                && Objects.equal(type, account.type)
                && Objects.equal(currency, account.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, description, type, currency);
    }

    @Override
    public String toString() {
        return "Account{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", currency=" + currency +
                '}';
    }

    public boolean isValid() {
        if (state == 1) {
            return title != null;
        }
        if (state == 2) {
            return title != null && description != null;
        }
        if (state == 3) {
            return title != null && description != null && type != null;
        }
        if (state == 4) {
            return title != null && description != null && type != null && currency != null;
        }
        return true;
    }
}
