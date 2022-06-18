package by.it.academy.account.scheduler.converter;

import by.it.academy.account.scheduler.dao.entity.ScheduledOperationEntity;
import by.it.academy.account.scheduler.model.Operation;
import by.it.academy.account.scheduler.model.Schedule;
import by.it.academy.account.scheduler.model.ScheduledOperation;
import by.it.academy.account.scheduler.view.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
