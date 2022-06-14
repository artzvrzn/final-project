package by.itacademy.account.controller.rest;

import by.itacademy.account.model.Operation;
import by.itacademy.account.model.OperationCriteria;
import by.itacademy.account.view.api.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/account/{uuid}/operation")
public class OperationController {

    @Autowired
    private OperationService operationService;

//    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public Page<Operation> get(@PathVariable("uuid") UUID accountId,
//                               @RequestParam("page") @Min(0) int page,
//                               @RequestParam("size") @Min(1) int size,
//                               @RequestParam(value = "from", required = false) LocalDateTime from,
//                               @RequestParam(value ="to", required = false) LocalDateTime to,
//                               @RequestParam(value = "cat", required = false) UUID[] categories) {
//        return operationService.get(accountId, page, size);
//    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Operation> get(@PathVariable("uuid") UUID accountId,
                               @RequestParam("page") @Min(0) int page,
                               @RequestParam("size") @Min(1) int size,
                               @Valid OperationCriteria criteria) {
        return operationService.get(accountId, page, size, criteria);
    }

    @GetMapping(value = {"/{uuid_operation}", "/{uuid_operation}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Operation get(@PathVariable("uuid") UUID accountId,
                         @PathVariable("uuid_operation") UUID operationId) {
        return operationService.get(accountId, operationId);
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable("uuid") UUID accountId,
                       @RequestBody @Valid Operation operation) {
        operationService.create(accountId, operation);
    }

    @PutMapping(value = {"/{uuid_operation}/dt_update/{dt_update}", "/{uuid_operation}/dt_update/{dt_update}/"},
                consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("uuid") UUID accountId,
                       @PathVariable("uuid_operation") UUID operationId,
                       @PathVariable("dt_update") LocalDateTime updated,
                       @RequestBody @Valid Operation operation) {
        operationService.update(accountId, operationId, updated, operation);
    }

    @DeleteMapping(value = {"/{uuid_operation}/dt_update/{dt_update}", "/{uuid_operation}/dt_update/{dt_update}/"},
                   consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") UUID accountId,
                       @PathVariable("uuid_operation") UUID operationId,
                       @PathVariable("dt_update") LocalDateTime updated) {
        operationService.delete(accountId, operationId, updated);
    }
}
