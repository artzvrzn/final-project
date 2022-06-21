package by.it.academy.report.dao.api;

import by.it.academy.report.dao.entity.ReportFilePropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface ReportFilePropertyRepository extends JpaRepository<ReportFilePropertyEntity, UUID> {

    @Transactional
    @Modifying
    @Query(value = "update app.report_properties set filename = :filename where report_id = :id", nativeQuery = true)
    void updateFilename(@Param("id") UUID id, @Param("filename") String filename);

}
