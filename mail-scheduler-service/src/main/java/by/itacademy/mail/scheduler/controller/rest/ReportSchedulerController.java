package by.itacademy.mail.scheduler.controller.rest;

import by.itacademy.mail.scheduler.model.ScheduledMail;
import by.itacademy.mail.scheduler.view.api.MailSchedulerService;
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
@RequestMapping("/scheduler/mail")
public class ReportSchedulerController {

    @Autowired
    private MailSchedulerService mailSchedulerService;

    @PostMapping(value = {"/report", "/report/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ScheduledMail scheduledMail) {
        mailSchedulerService.create(scheduledMail);
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<ScheduledMail> get(@RequestParam @Min(0) int page, @RequestParam @Min(1) int size) {
        return mailSchedulerService.get(page, size);
    }

    @GetMapping(value = {"/{uuid}", "/{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ScheduledMail get(@PathVariable("uuid") UUID id) {
        return mailSchedulerService.get(id);
    }

    @PutMapping(value = {"/{uuid}/dt_update/{dt_update}", "/{uuid}/dt_update/{dt_update}/"},
                consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("uuid") UUID id,
                       @PathVariable("dt_update") LocalDateTime updated,
                       @RequestBody ScheduledMail scheduledMail) {
        mailSchedulerService.update(id, updated, scheduledMail);
    }

    @DeleteMapping(value = {"/{uuid}/dt_update/{dt_update}", "/{uuid}/dt_update/{dt_update}/"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") UUID id,
                       @PathVariable("dt_update") LocalDateTime updated) {
        mailSchedulerService.delete(id, updated);
    }
}
