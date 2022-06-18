package by.itacademy.account.view;

import by.itacademy.account.dao.api.AccountRepository;
import by.itacademy.account.dao.entity.AccountEntity;
import by.itacademy.account.exception.RecordNotFoundException;
import by.itacademy.account.model.Account;
import by.itacademy.account.view.api.AccountService;
import by.itacademy.account.view.api.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private UserService userService;

    @Override
    public UUID create(Account dto) {
        String username = userService.getUserDetails().getUsername();
        AccountEntity entity = conversionService.convert(dto, AccountEntity.class);
        UUID id = UUID.randomUUID();
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        entity.setId(id);
        entity.setCreated(currentTime);
        entity.setUpdated(currentTime);
        entity.setUsername(username);
        accountRepository.save(entity);
        log.info("Account {} has been created", entity.getId());
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> get(int page, int size) {
        UserDetails userDetails = userService.getUserDetails();
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<AccountEntity> result;
        if (userService.isAdmin(userDetails)) {
            result = accountRepository.findAll(pageable);
        } else {
            result = accountRepository.findAllByUsername(userDetails.getUsername(), pageable);
        }
        return result != null ? result.map(e -> conversionService.convert(e, Account.class)) : null;
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
        if (!dto.getCurrency().equals(entity.getBalance().getCurrency())) {
            throw new IllegalArgumentException("currency changes is not allowed");
        }
        entity.setUpdated(LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        accountRepository.save(entity);
        log.info("Account {} has been updated", entity.getId());
    }

    private AccountEntity getOrThrow(UUID id) {
        UserDetails userDetails = userService.getUserDetails();
        Optional<AccountEntity> optional = accountRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(String.format("account %s doesn't exist", id));
        }
        AccountEntity entity = optional.get();
        if (!entity.getUsername().equals(userDetails.getUsername())) {
            if (!userService.isAdmin(userDetails)) {
                throw new AccessDeniedException("account belongs to another user");
            }
        }
        return optional.get();
    }

    private void checkUpdated(AccountEntity entity, LocalDateTime updated) {
        if (!entity.getUpdated().equals(updated)) {
            throw new IllegalArgumentException("account has already been updated");
        }
    }
}
