package by.itacademy.classifier.view.api;

import by.itacademy.classifier.model.BaseDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ClassifierService<T extends BaseDto> {

    void create(T dto);
    T get(UUID id);
    Page<T> get(int page, int size);

}
