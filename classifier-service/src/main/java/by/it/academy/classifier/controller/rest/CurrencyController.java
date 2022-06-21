package by.it.academy.classifier.controller.rest;

import by.it.academy.classifier.model.Currency;
import by.it.academy.classifier.view.api.ClassifierService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.UUID;

@RestController
@RequestMapping("/classifier/currency")
public class CurrencyController {

    private final ClassifierService<Currency> currencyService;

    public CurrencyController(ClassifierService<Currency> currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Currency currency) {
        currencyService.create(currency);
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Currency> get(@RequestParam @Min(0) int page, @Min(1) @RequestParam int size) {
        return currencyService.get(page, size);
    }

    @GetMapping(value = {"/{uuid}", "/{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Currency get(@PathVariable("uuid") UUID uuid) {
        return currencyService.get(uuid);
    }
}
