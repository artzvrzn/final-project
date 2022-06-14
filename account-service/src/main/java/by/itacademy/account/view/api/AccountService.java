package by.itacademy.account.view.api;

import by.itacademy.account.model.Account;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountService {

    UUID create(Account dto);

    Page<Account> get(int page, int size);

    Account get(UUID id);

    void update(UUID id, LocalDateTime updated, Account dto);
}
