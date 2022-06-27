package by.itacademy.telegram.view.api;

import by.itacademy.telegram.model.Account;
import by.itacademy.telegram.model.Category;
import by.itacademy.telegram.model.Currency;
import by.itacademy.telegram.model.Operation;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.UUID;

public interface CommunicatorService {

    void postAccount(Account account, String chatId) throws HttpStatusCodeException;

    Account getAccount(UUID id, String chatId) throws HttpStatusCodeException;

    List<Account> getAccounts(String chatId) throws HttpStatusCodeException;

    void updateAccount(Account account, String chatId) throws HttpStatusCodeException;

    Operation getOperation(UUID id, String chatId) throws HttpStatusCodeException;

    List<Operation> getOperations(String chatId) throws HttpStatusCodeException;

    void postOperation(UUID accountId, Operation operation, String chatId) throws HttpStatusCodeException;

    void updateOperation(UUID accountId, Operation operation, String ChatId) throws HttpStatusCodeException;

    void deleteOperation(UUID accountId, Operation operation, String chatId) throws HttpStatusCodeException;

    Currency getCurrency(UUID id, String chatId) throws HttpStatusCodeException;

    Currency getCurrency(String title, String chatId) throws HttpStatusCodeException;

    Category getCategory(UUID id, String chatId) throws HttpStatusCodeException;

    Category getCategory(String title, String chatId) throws HttpStatusCodeException;

    List<Category> getCategories(String chatId);
}
