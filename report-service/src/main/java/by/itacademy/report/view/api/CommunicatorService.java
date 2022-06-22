package by.itacademy.report.view.api;

import by.itacademy.report.model.Account;
import by.itacademy.report.model.Category;
import by.itacademy.report.model.Currency;
import by.itacademy.report.model.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CommunicatorService {

    Category getCategory(UUID id);

    Currency getCurrency(UUID id);

    Account getAccount(UUID id);

    List<Operation> getOperations(UUID accountId, LocalDate from, LocalDate to, List<UUID> categories);
}
