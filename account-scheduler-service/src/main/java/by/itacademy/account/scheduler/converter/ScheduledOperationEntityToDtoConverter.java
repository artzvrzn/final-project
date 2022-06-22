package by.itacademy.account.scheduler.converter;

import by.itacademy.account.scheduler.dao.entity.ScheduledOperationEntity;
import by.itacademy.account.scheduler.model.Operation;
import by.itacademy.account.scheduler.model.Schedule;
import by.itacademy.account.scheduler.model.ScheduledOperation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduledOperationEntityToDtoConverter implements Converter<ScheduledOperationEntity, ScheduledOperation> {

    @Override
    public ScheduledOperation convert(ScheduledOperationEntity entity) {
        ScheduledOperation dto = new ScheduledOperation();
        dto.setId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        Schedule schedule = new Schedule();
        schedule.setStartTime(entity.getStartTime());
        schedule.setStopTime(entity.getStopTime());
        schedule.setInterval(entity.getInterval());
        schedule.setTimeUnit(entity.getTimeUnit());
        Operation operation = new Operation();
        operation.setDescription(entity.getDescription());
        operation.setAccount(entity.getAccount());
        operation.setCategory(entity.getCategory());
        operation.setCurrency(entity.getCurrency());
        operation.setValue(entity.getValue());
        dto.setSchedule(schedule);
        dto.setOperation(operation);
        return dto;
    }
}
