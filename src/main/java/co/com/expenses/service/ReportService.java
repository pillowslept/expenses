package co.com.expenses.service;

import java.io.ByteArrayInputStream;
import java.time.Month;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.PdfReport;
import co.com.expenses.dto.PdfInformation;
import co.com.expenses.model.Movement;
import co.com.expenses.component.DateUtilities;

@Service
@Transactional
public class ReportService {

    @Autowired
    MovementService movementService;

    @Autowired
    PdfReport generatePdfReport;

    @Autowired
    DateUtilities dateUtilities;

    public ByteArrayInputStream generate() {
        List<Movement> movements = movementService.findAllByOrderByCreationDateAsc();
        PdfInformation pdfInformation = validatePdfInformation(0);
        return generatePdfReport.generate(movements, pdfInformation);
    }

    public ByteArrayInputStream byMonth(int month) {
        List<Movement> movements = movementService.findByCreationDateBetween(month);
        PdfInformation pdfInformation = validatePdfInformation(month);
        return generatePdfReport.generate(movements, pdfInformation);
    }

    public ByteArrayInputStream byYear(int year) {
        List<Movement> movements = movementService.findByCreationDateOfYear(year);
        PdfInformation pdfInformation = validatePdfInformationByYear(year);
        return generatePdfReport.generate(movements, pdfInformation);
    }

    public ByteArrayInputStream byMonthAndYear(int month, int year) {
        List<Movement> movements = movementService.findByCreationDateBetween(month, year);
        PdfInformation pdfInformation = validatePdfInformationByMonthAndYear(month, year);
        return generatePdfReport.generate(movements, pdfInformation);
    }

    private PdfInformation validatePdfInformation(int month) {
        return buildPdfInformation(dateUtilities.obtainBeginingOfDate(month), dateUtilities.obtainEndOfDate(month));
    }

    private PdfInformation validatePdfInformationByYear(int year) {
        return buildPdfInformation(dateUtilities.obtainBeginingOfDate(Month.JANUARY.getValue(), year),
                dateUtilities.obtainEndOfDate(Month.DECEMBER.getValue(), year));
    }

    private PdfInformation validatePdfInformationByMonthAndYear(int month, int year) {
        return buildPdfInformation(dateUtilities.obtainBeginingOfDate(month, year),
                dateUtilities.obtainEndOfDate(month, year));
    }

    private PdfInformation buildPdfInformation(Date startDate, Date endDate) {
        PdfInformation pdfInformation = new PdfInformation();
        pdfInformation.setUserName("Juan Camilo Velásquez");
        pdfInformation.setStartDate(dateUtilities.dateToString(startDate));
        pdfInformation.setEndDate(dateUtilities.dateToString(endDate));
        return pdfInformation;
    }

}
