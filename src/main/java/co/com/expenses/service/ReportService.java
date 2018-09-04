package co.com.expenses.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.PdfReport;
import co.com.expenses.model.Movement;

@Service
@Transactional
public class ReportService {

    @Autowired
    MovementService movementService;

    @Autowired
    PdfReport generatePdfReport;

    public ByteArrayInputStream generate() {
        List<Movement> movements = movementService.findAllByOrderByCreationDateAsc();
        return generatePdfReport.generate(movements);
    }

    public ByteArrayInputStream byMonth(int month) {
        List<Movement> movements = movementService.findByCreationDateBetween(month);
        return generatePdfReport.generate(movements);
    }
}
