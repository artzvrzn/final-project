package by.itacademy.account.view;

import by.itacademy.account.dao.api.OperationRepository;
import by.itacademy.account.dao.api.OperationSpecifications;
import by.itacademy.account.dao.entity.AccountEntity;
import by.itacademy.account.dao.entity.OperationEntity;
import by.itacademy.account.exception.RecordNotFoundException;
import by.itacademy.account.model.Operation;
import by.itacademy.account.model.OperationCriteria;
import by.itacademy.account.view.api.AccountService;
import by.itacademy.account.view.api.OperationService;
import by.itacademy.account.view.api.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private UserService userService;

    @Override
    public UUID create(UUID accountId, Operation dto) {
        checkAuthorization(accountId);
        OperationEntity operationEntity = conversionService.convert(dto, OperationEntity.class);
        AccountEntity accountEntity = conversionService.convert(accountService.get(accountId), AccountEntity.class);
        checkCurrency(dto.getCurrency(), accountEntity.getBalance().getCurrency());
        UUID id = UUID.randomUUID();
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        operationEntity.setId(id);
        operationEntity.setCreated(currentTime);
        operationEntity.setUpdated(currentTime);
        operationEntity.setAccount(accountEntity);
        operationRepository.save(operationEntity);
        log.info("Operation {} has been created. Account id {}", operationEntity.getId(), accountId);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Operation> get(UUID accountId, int page, int size, OperationCriteria criteria) {
        checkAuthorization(accountId);
        Sort sort = Sort.by("date");
        if (criteria.getSort().equals(Sort.Direction.ASC)) {
            sort.ascending();
        } else {
            sort.descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        UserDetails userDetails = userService.getUserDetails();
        Page<Operation> result;
        if (userService.isAdmin(userDetails)) {
            result = operationRepository.findAll(OperationSpecifications.byIdAndCriteria(accountId, criteria), pageable)
                    .map(e -> conversionService.convert(e, Operation.class));
        } else {
            result = operationRepository
                    .findAll(OperationSpecifications.byIdAndUsernameAndCriteria
                            (accountId, userDetails.getUsername(), criteria), pageable)
                    .map(e -> conversionService.convert(e, Operation.class));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Operation get(UUID accountId, UUID operationId) {
        OperationEntity entity = getOrThrow(accountId, operationId);
        return conversionService.convert(entity, Operation.class);
    }

    @Override
    public void update(UUID accountId, UUID operationId, LocalDateTime updated, Operation dto) {
        OperationEntity entity = getOrThrow(accountId, operationId);
        checkUpdated(entity, updated);
        checkCurrency(dto.getCurrency(), entity.getAccount().getBalance().getCurrency());
        entity.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
        entity.setCategory(dto.getCategory());
//        entity.setCurrency(dto.getCurrency());
        entity.setDate(dto.getDate());
        entity.setValue(dto.getValue());
        entity.setDescription(dto.getDescription());
        operationRepository.save(entity);
        log.info("Operation {} has been updated. Account id {}", operationId, accountId);

    }

    @Override
    public void delete(UUID accountId, UUID operationId, LocalDateTime updated) {
        OperationEntity entity = getOrThrow(accountId, operationId);
        checkUpdated(entity, updated);
        operationRepository.delete(entity);
        log.info("Operation {} has been deleted. Account id {}", operationId, accountId);
    }

    private OperationEntity getOrThrow(UUID accountId, UUID operationId) {
        checkAuthorization(accountId);
        Optional<OperationEntity> optional = operationRepository.findByAccount_IdAndId(accountId, operationId);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(String.format("Operation with id %s doesn't exist", operationId));
        }
        return optional.get();
    }

    private void checkUpdated(OperationEntity entity, LocalDateTime updated) {
        if (!entity.getUpdated().equals(updated)) {
            throw new IllegalArgumentException("Operation has already been updated");
        }
    }

    private void checkCurrency(UUID operationCurrency, UUID accountCurrency) {
        if (!operationCurrency.equals(accountCurrency)) {
            throw new IllegalArgumentException(
                    String.format("Incorrect currency %s. Account currency is %s", operationCurrency, accountCurrency));
        }
    }

    private void checkAuthorization(UUID accountId) {
        accountService.get(accountId);
    }
}
