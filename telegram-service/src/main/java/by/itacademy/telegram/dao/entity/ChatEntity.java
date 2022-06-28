package by.itacademy.telegram.dao.entity;

import by.itacademy.telegram.model.constant.MenuState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "telegram_chat", schema = "app")
public class ChatEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private String id;
    @Column(nullable = false)
    private MenuState menuState;
    private UUID chosenAccount;
    private UUID chosenOperation;
    @Column(columnDefinition = "character varying")
    private String jwtToken;
}
