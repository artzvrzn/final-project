package by.it.academy.report.view.api;

import by.it.academy.report.model.Account;
import by.it.academy.report.model.Category;
import by.it.academy.report.model.Currency;
import by.it.academy.report.model.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CommunicatorService {

    Category getCategory(UUID id);

    Currency getCurrency(UUID id);

    Account getAccount(UUID id);

    List<Operation> getOperations(UUID accountId, LocalDate from, LocalDate to, List<UUID> categories);
}
