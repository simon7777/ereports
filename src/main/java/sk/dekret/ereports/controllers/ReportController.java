package sk.dekret.ereports.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.dekret.ereports.models.DayReports;
import sk.dekret.ereports.models.Report;
import sk.dekret.ereports.models.ResponseResultList;
import sk.dekret.ereports.services.ReportService;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${app.client.url}"})
public class ReportController {

    private final ReportService reportService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Report> createReport(@Valid @RequestBody Report report) {
        return new ResponseEntity<>(this.reportService.createReport(report), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Report> updateReport(@PathVariable("id") Long id, @Valid @RequestBody Report report) {
        return ResponseEntity.ok(this.reportService.updateReport(id, report));
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> deleteReport(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.reportService.deleteReport(id));
    }

    @GetMapping(path = "/byUser/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseResultList<DayReports>> findReportsByUser(@PathVariable("userId") Long id, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return ResponseEntity.ok(this.reportService.findReportsByUserId(id, page, pageSize));
    }

    @GetMapping(path = "/byCurrentUser", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseResultList<DayReports>> findReportsByCurrentUser(@RequestParam("page") Integer page,
                                                                                   @RequestParam("pageSize") Integer pageSize) {
        return ResponseEntity.ok(this.reportService.findReportsForCurrentUser(page, pageSize));
    }
}
