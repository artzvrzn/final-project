package by.itacademy.account.model;

import by.itacademy.account.validation.anno.Exist;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private SortOrder sort = SortOrder.DESC;
}
