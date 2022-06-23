package by.itacademy.mail.scheduler.dao.api;

import by.itacademy.mail.scheduler.dao.entity.ScheduledMailReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduledMailRepository extends JpaRepository<ScheduledMailReportEntity, UUID> {

    Page<ScheduledMailReportEntity> findAllByUsername(String username, Pageable pageable);
}
