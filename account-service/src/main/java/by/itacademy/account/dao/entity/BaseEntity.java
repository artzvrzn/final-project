package by.itacademy.account.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Column(updatable = false)
    private UUID id;
    @Column(updatable = false, columnDefinition = "timestamp(3)")
    private LocalDateTime created;
    @Column(columnDefinition = "timestamp(3)")
    private LocalDateTime updated;
}
