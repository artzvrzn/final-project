package by.itacademy.telegram.model.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
            return INVALID;
        }
    }
}
