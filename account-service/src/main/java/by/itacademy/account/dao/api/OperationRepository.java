package by.itacademy.account.dao.api;

import by.itacademy.account.dao.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationRepository
        extends JpaRepository<OperationEntity, UUID>, JpaSpecificationExecutor<OperationEntity> {

    Optional<OperationEntity> findByAccount_IdAndId(UUID accountId, UUID id);

}
