package by.itacademy.classifier.dao.api;

import by.itacademy.classifier.dao.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, UUID> {

//    @Query("select case when count(c) > 0 then true else false end from Currency c where title = :title")
    boolean existsByTitle(String title);
}
