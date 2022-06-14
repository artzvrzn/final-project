package by.itacademy.account.converter;

import by.itacademy.account.dao.entity.OperationEntity;
import by.itacademy.account.model.Operation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OperationEntityToDtoConverter implements Converter<OperationEntity, Operation> {

    @Override
    public Operation convert(OperationEntity entity) {
        Operation operation = new Operation();
        operation.setId(entity.getId());
        operation.setCreated(entity.getCreated());
        operation.setUpdated(entity.getUpdated());
        operation.setCategory(entity.getCategory());
        operation.setCurrency(entity.getCurrency());
        operation.setDate(entity.getDate());
        operation.setDescription(entity.getDescription());
        operation.setValue(entity.getValue());
        return operation;
    }
}
