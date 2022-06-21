package by.itacademy.account.dao.api;

import by.itacademy.account.dao.entity.OperationEntity;
import by.itacademy.account.model.OperationCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class OperationSpecifications {

    public static Specification<OperationEntity> byIdAndUsernameAndCriteria
            (UUID accountId, String username, OperationCriteria criteria) {
        return Specification.where(accountIdEquals(accountId))
                .and(usernameEquals(username))
                .and(dateAfter(criteria.getFrom()))
                .and(dateBefore(criteria.getTo()))
                .and(belongsToCategory(criteria.getCategory()));
    }

    public static Specification<OperationEntity> byIdAndCriteria (UUID accountId, OperationCriteria criteria) {
        return Specification.where(accountIdEquals(accountId))
                .and(dateAfter(criteria.getFrom()))
                .and(dateBefore(criteria.getTo()))
                .and(belongsToCategory(criteria.getCategory()));
    }

    public static Specification<OperationEntity> usernameEquals(String username) {
        return (root, query, cb) -> cb.equal(root.get("account").get("username"), username);
    }

    public static Specification<OperationEntity> accountIdEquals(UUID id) {
        return (root, query, cb) -> cb.equal(root.get("account").get("id"), id);
    }

    public static Specification<OperationEntity> dateAfter(LocalDate from) {
        if (from == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThan(root.get("date"), from);
    }

    public static Specification<OperationEntity> dateBefore(LocalDate to) {
        if (to == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThan(root.get("date"), to);
    }

    public static Specification<OperationEntity> belongsToCategory(List<UUID> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> root.get("category").in(categories);
    }
}
