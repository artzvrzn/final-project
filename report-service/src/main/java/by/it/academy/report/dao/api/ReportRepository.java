package by.it.academy.report.dao.api;

import by.it.academy.report.dao.entity.ReportEntity;
import by.it.academy.report.model.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {

    @EntityGraph(attributePaths = {"fileProperty"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<ReportEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"fileProperty"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<ReportEntity> findAllByUsername(String username, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update app.reports " +
            "set status = :#{#status?.ordinal()}, updated = :dt_update " +
            "where id = :id", nativeQuery = true)
    void updateStatus
            (@Param("id") UUID id, @Param("status") ReportStatus status, @Param("dt_update") LocalDateTime update);
}
