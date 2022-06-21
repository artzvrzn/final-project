package by.it.academy.report.controller.rest;

import by.it.academy.report.model.Report;
import by.it.academy.report.model.ReportType;
import by.it.academy.report.view.api.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping(value = {"/{type}", "/{type}/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable("type") ReportType type,
                       @RequestBody Map<String, Object> params) {
        reportService.create(type, params);
    }

    @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Report> get(@RequestParam("page") int page, @RequestParam("size") int size) {
        return reportService.get(page, size);
    }

}
