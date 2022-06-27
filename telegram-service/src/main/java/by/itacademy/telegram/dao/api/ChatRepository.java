package by.itacademy.telegram.dao.api;

import by.itacademy.telegram.dao.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, String> {
}
