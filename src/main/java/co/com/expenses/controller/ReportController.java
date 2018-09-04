package co.com.expenses.controller;

import static co.com.expenses.util.Constants.PDF_FILE_EXTENSION;

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
import co.com.expenses.service.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private static final String REPORT_NAME_BY_REQUEST = "reporte-movimientos";

    @Autowired
    ReportService reportService;

    @Autowired
    FileUtilities fileUtilities;

    @RequestMapping(value = "/generate", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byRequest() {
        return buildResponse(REPORT_NAME_BY_REQUEST, reportService.generate());
    }

    @RequestMapping(value = "/byMonth/{month}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byMonth(@PathVariable("month") int month) {
        return buildResponse(REPORT_NAME_BY_REQUEST, reportService.byMonth(month));
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