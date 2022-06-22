package by.itacademy.account.scheduler.controller.rest;

import by.itacademy.account.scheduler.model.ScheduledOperation;
import by.itacademy.account.scheduler.view.api.OperationSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/scheduler/operation")
public class SchedulerController {

    @Autowired
    private OperationSchedulerService operationSchedulerService;

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ScheduledOperation scheduledOperation) {
        operationSchedulerService.create(scheduledOperation);
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<ScheduledOperation> get(@RequestParam @Min(0) int page, @RequestParam @Min(1) int size) {
        return operationSchedulerService.get(page, size);
    }

    @GetMapping(value = {"/{uuid}", "/{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ScheduledOperation get(@PathVariable("uuid") UUID id) {
        return operationSchedulerService.get(id);
    }

    @PutMapping(value = {"/{uuid}/dt_update/{dt_update}", "/{uuid}/dt_update/{dt_update}/"},
                consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("uuid") UUID id,
                       @PathVariable("dt_update") LocalDateTime updated,
                       @RequestBody @Valid ScheduledOperation scheduledOperation) {
        operationSchedulerService.update(id, updated, scheduledOperation);
    }

    @DeleteMapping(value = {"/{uuid}/dt_update/{dt_update}", "/{uuid}/dt_update/{dt_update}/"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") UUID id,
                       @PathVariable("dt_update") LocalDateTime updated) {
        operationSchedulerService.delete(id, updated);
    }
}
