package co.com.expenses.controller;

import static co.com.expenses.util.Constants.PDF_FILE_EXTENSION;
import static co.com.expenses.util.Constants.REPORT_NAME_BY_REQUEST;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.expenses.component.FileUtilities;
import co.com.expenses.enums.ReportType;
import co.com.expenses.service.ReportService;

@RestController
@RequestMapping("/api/pdf/reports")
public class PdfReportsController {

    @Autowired
    ReportService reportService;

    @Autowired
    FileUtilities fileUtilities;

    @RequestMapping(value = "/generate", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byRequest() {
        return buildResponse(REPORT_NAME_BY_REQUEST, reportService.generate(ReportType.PDF));
    }

    @RequestMapping(value = "/month/{month:[1-9]|1[0-2]}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byMonth(@PathVariable("month") int month) {
        return buildResponse(REPORT_NAME_BY_REQUEST, reportService.byMonth(month, ReportType.PDF));
    }

    @RequestMapping(value = "/year/{year:^[0-9]{4}$}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byYear(@PathVariable("year") int year) {
        return buildResponse(REPORT_NAME_BY_REQUEST, reportService.byYear(year, ReportType.PDF));
    }

    @RequestMapping(value = "/month/{month:[1-9]|1[0-2]}/year/{year:^[0-9]{4}$}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byMonthAndYear(@PathVariable("month") int month,
            @PathVariable("year") int year) {
        return buildResponse(REPORT_NAME_BY_REQUEST, reportService.byMonthAndYear(month, year, ReportType.PDF));
    }

    private ResponseEntity<InputStreamResource> buildResponse(String name,
            ByteArrayInputStream bis) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename="
                        + fileUtilities.createFileName(name,
                                PDF_FILE_EXTENSION));

        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}