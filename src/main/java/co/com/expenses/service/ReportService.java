package co.com.expenses.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.PdfReport;
import co.com.expenses.dto.PdfInformation;
import co.com.expenses.model.Movement;
import co.com.expenses.util.DateUtilities;

@Service
@Transactional
public class ReportService {

    @Autowired
    MovementService movementService;

    @Autowired
    PdfReport generatePdfReport;

    public ByteArrayInputStream generate() {
        List<Movement> movements = movementService.findAllByOrderByCreationDateAsc();
        PdfInformation pdfInformation = buildPdfInformation(0);
        return generatePdfReport.generate(movements, pdfInformation);
    }

    public ByteArrayInputStream byMonth(int month) {
        List<Movement> movements = movementService.findByCreationDateBetween(month);
        PdfInformation pdfInformation = buildPdfInformation(month);
        return generatePdfReport.generate(movements, pdfInformation);
    }

    public ByteArrayInputStream byMonthAndYear(int month, int year) {
        List<Movement> movements = movementService.findByCreationDateBetween(month, year);
        PdfInformation pdfInformation = buildPdfInformation(month);
        return generatePdfReport.generate(movements, pdfInformation);
    }

    private PdfInformation buildPdfInformation(int month) {
        PdfInformation pdfInformation = new PdfInformation();
        pdfInformation.setUserName("Juan Camilo Velásquez");
        pdfInformation.setStartDate(DateUtilities.dateToString(DateUtilities.obtainBeginingOfDate(month)));
        pdfInformation.setEndDate(DateUtilities.dateToString(DateUtilities.obtainEndOfDate(month)));
        return pdfInformation;
    }

}
