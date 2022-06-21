package by.it.academy.report.controller.rest;

import by.it.academy.report.model.FileData;
import by.it.academy.report.view.api.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/account/{uuid}/export")
public class ExportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> get(@PathVariable("uuid") UUID id) throws IOException {
        FileData file = reportService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .contentType(file.getMediaType())
                .body(file.getContent());
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.HEAD)
    public ResponseEntity<?> isAvailable(@PathVariable("uuid") UUID id) {
        if (reportService.isReady(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
