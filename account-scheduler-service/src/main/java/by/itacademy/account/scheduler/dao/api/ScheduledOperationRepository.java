package by.itacademy.account.scheduler.dao.api;

import by.itacademy.account.scheduler.dao.entity.ScheduledOperationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduledOperationRepository extends JpaRepository<ScheduledOperationEntity, UUID> {

    Page<ScheduledOperationEntity> findAllByUsername(String username, Pageable pageable);
}
