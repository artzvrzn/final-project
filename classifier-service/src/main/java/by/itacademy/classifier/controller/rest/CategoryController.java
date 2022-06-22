package by.itacademy.classifier.controller.rest;

import by.itacademy.classifier.model.Category;
import by.itacademy.classifier.view.api.ClassifierService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.UUID;

@RestController
@RequestMapping("/classifier/operation/category")
public class CategoryController {

    private final ClassifierService<Category> categoryService;

    public CategoryController(ClassifierService<Category> categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Category category) {
        categoryService.create(category);
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Category> get(@RequestParam @Min(0) int page, @Min(1) @RequestParam int size) {
        return categoryService.get(page, size);
    }

    @GetMapping(value = {"/{uuid}", "/{uuid}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Category get(@PathVariable("uuid") UUID uuid) {
        return categoryService.get(uuid);
    }
}
