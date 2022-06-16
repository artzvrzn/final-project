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
@Table(name = "category", schema = "app")
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;
}
