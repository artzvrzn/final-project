package by.itacademy.classifier.dao.api;

import by.itacademy.classifier.dao.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

//    @Query("select case when count(c) > 0 then true else false end from Category c where title = :title")
    boolean existsByTitle(String title);
}
