package by.itacademy.account.dao.api;

import by.itacademy.account.dao.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    @EntityGraph(attributePaths = {"balance"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<AccountEntity> findAll(Pageable pageable);
}
