package by.itacademy.account.model;

import by.itacademy.account.validation.anno.Exist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OperationCriteria {

    private LocalDate from;
    private LocalDate to;
    @Exist(Exist.Classifier.CATEGORY)
    private List<UUID> category;
    private Sort.Direction sort = Sort.Direction.DESC;
}
