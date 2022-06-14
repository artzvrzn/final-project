package by.itacademy.account.view;

import by.itacademy.account.dao.api.AccountRepository;
import by.itacademy.account.dao.entity.AccountEntity;
import by.itacademy.account.exception.RecordNotFoundException;
import by.itacademy.account.model.Account;
import by.itacademy.account.view.api.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements AccountService {

    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public ConversionService conversionService;

    @Override
    public UUID create(Account dto) {
        AccountEntity entity = conversionService.convert(dto, AccountEntity.class);
        UUID id = UUID.randomUUID();
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        entity.setId(id);
        entity.setCreated(currentTime);
        entity.setUpdated(currentTime);
        accountRepository.save(entity);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> get(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        return accountRepository.findAll(pageable).map(e -> conversionService.convert(e, Account.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Account get(UUID id) {
        return conversionService.convert(getOrThrow(id), Account.class);
    }

    @Override
    public void update(UUID id, LocalDateTime updated, Account dto) {
        AccountEntity entity = getOrThrow(id);
        checkUpdated(entity, updated);
        if (dto.getCurrency().equals(entity.getBalance().getCurrency())) {
            throw new IllegalArgumentException("Currency change is not allowed");
        }
        entity.setUpdated(LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        accountRepository.save(entity);
    }

    private AccountEntity getOrThrow(UUID id) {
        Optional<AccountEntity> optional = accountRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(String.format("Account %s doesn't exist", id));
        }
        return optional.get();
    }

    private void checkUpdated(AccountEntity entity, LocalDateTime updated) {
        if (!entity.getUpdated().equals(updated)) {
            throw new IllegalArgumentException("Account has already been updated");
        }
    }
}
