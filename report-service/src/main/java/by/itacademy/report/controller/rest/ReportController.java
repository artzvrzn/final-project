package by.itacademy.report.controller.rest;

import by.itacademy.report.model.Report;
import by.itacademy.report.model.ReportType;
import by.itacademy.report.view.api.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping(value = {"/{type}", "/{type}/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@PathVariable("type") ReportType type,
                                    @RequestBody Map<String, Object> params) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("id", reportService.create(type, params).toString())
                .build();
    }

    @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Report> get(@RequestParam("page") int page, @RequestParam("size") int size) {
        return reportService.get(page, size);
    }
}
