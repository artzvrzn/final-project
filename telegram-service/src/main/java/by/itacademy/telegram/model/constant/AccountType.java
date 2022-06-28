package by.itacademy.telegram.model.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    public static String getAvailableTypes() {
        return Arrays
                .stream(AccountType.values())
                .filter(e -> e.equals(INVALID))
                .map(AccountType::name)
                .collect(Collectors.joining("\n"));
    }
}
