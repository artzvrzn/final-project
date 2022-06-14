package by.itacademy.account.controller.rest;

import by.itacademy.account.model.Account;
import by.itacademy.account.view.AccountServiceImpl;
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
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Account> get(@RequestParam(name = "page") @Min(0) int page,
                             @RequestParam(name = "size") @Min(1) int size) {
        return accountService.get(page, size);
    }

    @GetMapping(value = {"/{uuid}", "/{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Account get(@PathVariable UUID uuid) {
        return accountService.get(uuid);
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Account account) {
        accountService.create(account);
    }

    @PutMapping(value = {"/{uuid}/dt_update/{dt_update}", "/{uuid}/dt_update/{dt_update}/"},
                consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("uuid") UUID uuid,
                       @PathVariable("dt_update") LocalDateTime updated,
                       @RequestBody @Valid Account account) {
        accountService.update(uuid, updated, account);
    }

}
