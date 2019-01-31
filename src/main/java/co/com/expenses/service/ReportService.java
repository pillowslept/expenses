package co.com.expenses.service;

import java.io.ByteArrayInputStream;
import java.time.Month;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.DateUtilities;
import co.com.expenses.component.ExcelReport;
import co.com.expenses.component.PdfReport;
import co.com.expenses.dto.ReportInformation;
import co.com.expenses.enums.ReportType;
import co.com.expenses.model.Movement;

@Service
@Transactional
public class ReportService {

    @Autowired
    MovementService movementService;

    @Autowired
    PdfReport pdfReport;

    @Autowired
    ExcelReport excelReport;

    @Autowired
    DateUtilities dateUtilities;

    public ByteArrayInputStream generate(ReportType reportType) {
        List<Movement> movements = movementService.findAllByOrderByCreationDateAsc();
        ReportInformation reportInformation = validateInformation(0);
        return generateReportByType(movements, reportInformation, reportType);
    }

    public ByteArrayInputStream byMonth(int month, ReportType reportType) {
        List<Movement> movements = movementService.findByCreationDateBetween(month);
        ReportInformation reportInformation = validateInformation(month);
        return generateReportByType(movements, reportInformation, reportType);
    }

    public ByteArrayInputStream byYear(int year, ReportType reportType) {
        List<Movement> movements = movementService.findByCreationDateOfYear(year);
        ReportInformation reportInformation = validateInformationByYear(year);
        return generateReportByType(movements, reportInformation, reportType);
    }

    public ByteArrayInputStream byMonthAndYear(int month, int year, ReportType reportType) {
        List<Movement> movements = movementService.findByCreationDateBetween(month, year);
        ReportInformation reportInformation = validateInformationByMonthAndYear(month, year);
        return generateReportByType(movements, reportInformation, reportType);
    }

    private ReportInformation validateInformation(int month) {
        return buildReportInformation(dateUtilities.obtainBeginingOfDate(month), dateUtilities.obtainEndOfDate(month));
    }

    private ReportInformation validateInformationByYear(int year) {
        return buildReportInformation(dateUtilities.obtainBeginingOfDate(Month.JANUARY.getValue(), year),
                dateUtilities.obtainEndOfDate(Month.DECEMBER.getValue(), year));
    }

    private ReportInformation validateInformationByMonthAndYear(int month, int year) {
        return buildReportInformation(dateUtilities.obtainBeginingOfDate(month, year),
                dateUtilities.obtainEndOfDate(month, year));
    }

    private ReportInformation buildReportInformation(Date startDate, Date endDate) {
        ReportInformation reportInformation = new ReportInformation();
        reportInformation.setUserName("Juan Camilo Velásquez");
        reportInformation.setStartDate(dateUtilities.dateToString(startDate));
        reportInformation.setEndDate(dateUtilities.dateToString(endDate));
        return reportInformation;
    }

    private ByteArrayInputStream generateReportByType(List<Movement> movements, ReportInformation reportInformation,
            ReportType reportType) {
        ByteArrayInputStream byteArrayInputStream = null;
        if (reportType.equals(ReportType.EXCEL)) {
            byteArrayInputStream = excelReport.generate(movements, reportInformation);
        } else {
            byteArrayInputStream = pdfReport.generate(movements, reportInformation);
        }
        return byteArrayInputStream;
    }

}
