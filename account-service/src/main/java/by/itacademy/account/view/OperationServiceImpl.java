package by.itacademy.account.view;

import by.itacademy.account.dao.api.OperationRepository;
import by.itacademy.account.dao.api.OperationSpecifications;
import by.itacademy.account.dao.entity.AccountEntity;
import by.itacademy.account.dao.entity.OperationEntity;
import by.itacademy.account.exception.RecordNotFoundException;
import by.itacademy.account.model.Operation;
import by.itacademy.account.model.OperationCriteria;
import by.itacademy.account.model.SortOrder;
import by.itacademy.account.view.api.AccountService;
import by.itacademy.account.view.api.OperationService;
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
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ConversionService conversionService;

    @Override
    public UUID create(UUID accountId, Operation dto) {
        OperationEntity operationEntity = conversionService.convert(dto, OperationEntity.class);
        AccountEntity accountEntity = conversionService.convert(accountService.get(accountId), AccountEntity.class);
        UUID id = UUID.randomUUID();
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        operationEntity.setId(id);
        operationEntity.setCreated(currentTime);
        operationEntity.setUpdated(currentTime);
        operationEntity.setAccount(accountEntity);
        operationRepository.save(operationEntity);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Operation> get(UUID accountId, int page, int size, OperationCriteria criteria) {
        Sort sort = Sort.by("date");
        if (criteria.getSort().equals(SortOrder.ASC)) {
            sort.ascending();
        } else {
            sort.descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return operationRepository.findAll(OperationSpecifications.byCriteria(accountId, criteria), pageable)
                .map(e -> conversionService.convert(e, Operation.class));
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
        checkCurrency(entity, dto.getCurrency());
        entity.setUpdated(LocalDateTime.now(ZoneOffset.UTC));
        entity.setCategory(dto.getCategory());
        checkCurrency(entity, dto.getCurrency());
        entity.setCurrency(dto.getCurrency());
        entity.setDate(dto.getDate());
        entity.setValue(dto.getValue());
        entity.setDescription(dto.getDescription());
        operationRepository.save(entity);
    }

    @Override
    public void delete(UUID accountId, UUID operationId, LocalDateTime updated) {
        OperationEntity entity = getOrThrow(accountId, operationId);
        checkUpdated(entity, updated);
        operationRepository.delete(entity);
    }

    private OperationEntity getOrThrow(UUID accountId, UUID operationId) {
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

    private void checkCurrency(OperationEntity entity, UUID currency) {
        if (!entity.getCurrency().equals(entity.getAccount().getBalance().getCurrency())) {
            throw new IllegalArgumentException(
                    String.format("Incorrect currency %s. Account currency is %s",
                            currency, entity.getAccount().getBalance().getCurrency()));
        }
    }
}
