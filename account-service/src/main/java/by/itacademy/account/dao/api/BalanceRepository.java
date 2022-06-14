package by.itacademy.account.dao.api;

import by.itacademy.account.dao.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, UUID> {
    @Modifying
    @Query(value = "UPDATE app.balances AS b\n" +
            "SET value = o.sum_value\n" +
            "FROM (SELECT SUM(value) AS sum_value, account_id \n" +
            "\t  FROM app.operations \n" +
            "\t  GROUP BY account_id) AS o\n" +
            "WHERE b.account_id = o.account_id", nativeQuery = true)
    @Transactional
    void updateBalance();
}
