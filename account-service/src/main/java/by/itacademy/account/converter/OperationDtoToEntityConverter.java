package by.itacademy.account.converter;

import by.itacademy.account.dao.entity.OperationEntity;
import by.itacademy.account.model.Operation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OperationDtoToEntityConverter implements Converter<Operation, OperationEntity> {

    @Override
    public OperationEntity convert(Operation dto) {
        OperationEntity entity = new OperationEntity();
        entity.setId(dto.getId());
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        entity.setCategory(dto.getCategory());
        entity.setCurrency(dto.getCurrency());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        entity.setValue(dto.getValue());
        return entity;
    }
}
