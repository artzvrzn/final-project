package by.itacademy.account.view.api;

import by.itacademy.account.model.Operation;
import by.itacademy.account.model.OperationCriteria;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OperationService {

    UUID create(UUID accountId, Operation dto);

    Page<Operation> get(UUID accountId, int page, int size, OperationCriteria criteria);

    Operation get(UUID accountId, UUID operationId);

    void update(UUID accountId, UUID operationId, LocalDateTime updated, Operation dto);

    void delete(UUID accountId, UUID operationId, LocalDateTime updated);
}
