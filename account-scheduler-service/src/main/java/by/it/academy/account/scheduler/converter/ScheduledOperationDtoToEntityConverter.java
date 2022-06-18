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
public class ScheduledOperationDtoToEntityConverter implements Converter<ScheduledOperation, ScheduledOperationEntity> {

    @Autowired
    private UserService userService;

    @Override
    public ScheduledOperationEntity convert(ScheduledOperation dto) {
        ScheduledOperationEntity entity = new ScheduledOperationEntity();
        entity.setId(dto.getId());
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        Schedule schedule = dto.getSchedule();
        entity.setStartTime(schedule.getStartTime());
        entity.setStopTime(schedule.getStopTime());
        entity.setInterval(schedule.getInterval());
        entity.setTimeUnit(schedule.getTimeUnit());
        Operation operation = dto.getOperation();
        entity.setDescription(operation.getDescription());
        entity.setAccount(operation.getAccount());
        entity.setCategory(operation.getCategory());
        entity.setCurrency(operation.getCurrency());
        entity.setValue(operation.getValue());
        entity.setUsername(userService.getUserDetails().getUsername());
        return entity;
    }
}
