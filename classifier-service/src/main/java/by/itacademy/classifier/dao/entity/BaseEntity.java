package by.itacademy.classifier.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    private UUID id;
    @Column(nullable = false,updatable = false)
    private LocalDateTime created;
    @Column(nullable = false)
    private LocalDateTime updated;
}
