package by.itacademy.classifier.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "currency", schema = "app")
public class CurrencyEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private String description;
}
