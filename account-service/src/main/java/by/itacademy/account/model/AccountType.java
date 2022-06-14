package by.itacademy.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum AccountType {

    CASH, BANK_ACCOUNT, BANK_DEPOSIT, INVALID;

    @JsonCreator
    public static AccountType valueOfOrInvalid(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        try {
            return AccountType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return INVALID;
        }
    }
}
