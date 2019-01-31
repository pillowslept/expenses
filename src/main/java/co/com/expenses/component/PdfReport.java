package co.com.expenses.component;

import static co.com.expenses.util.CurrencyUtilities.formatValue;
import static co.com.expenses.util.PdfUtils.bodyCell;
import static co.com.expenses.util.PdfUtils.headCell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.com.expenses.dto.ChartSeries;
import co.com.expenses.dto.ReportInformation;
import co.com.expenses.dto.Summary;
import co.com.expenses.enums.Type;
import co.com.expenses.model.Movement;
import co.com.expenses.service.CategoryService;
import co.com.expenses.util.PdfUtils;

@Component
public class PdfReport {

    private static final String DATE_MESSAGE_FILTER = "Desde: %s Hasta: %s";
    private static final String TOTAL_TITLE = "Total";
    private static final String EXPENSES_TITLE = "Egresos";
    private static final String INCOMES_TITLE = "Ingresos";
    private static final String ERROR_GENERATING_PDF = "Ocurrió un error en la generación del PDF";
    private static final String REPORT_NAME = "REPORTE DE MOVIMIENTOS";

    private static final String LOGO = "/images/expenses.jpg";

    private static final Logger LOGGER = Logger.getLogger(PdfReport.class.getName());

    @Autowired
    Charts charts;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DateUtilities dateUtilities;

    public ByteArrayInputStream generate(List<Movement> movements, ReportInformation reportInformation) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.FIVE_COLUMNS);
            table.setWidths(new int[]{1, 1, 1, 5, 2});

            table.addCell(headCell("Tipo"));
            table.addCell(headCell("Categoría"));
            table.addCell(headCell("Valor"));
            table.addCell(headCell("Observaciones"));
            table.addCell(headCell("Fecha"));

            Summary summary = initializeSummary();
            HashMap<Long, String> incomeCategories = new HashMap<>();
            HashMap<Long, String> expenseCategories = new HashMap<>();

            for (Movement movement : movements) {
                table.addCell(bodyCell(movement.getType().getDescription()));
                table.addCell(bodyCell(movement.getCategory().getDescription()));
                table.addCell(bodyCell(formatValue(movement.getValue())));
                table.addCell(bodyCell(movement.getObservations()));
                table.addCell(bodyCell(dateUtilities.timestampToString(movement.getCreationDate())));

                recalculateSummary(summary, movement);
                validateCategories(movement, incomeCategories, expenseCategories);
            }

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(createHeader(REPORT_NAME, reportInformation));
            document.add(table);
            document.add(createSummary(summary));
            document.add(printCharts(movements, incomeCategories, expenseCategories));
            document.close();
        } catch (DocumentException ex) {
            LOGGER.error(String.format(ERROR_GENERATING_PDF), ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void validateCategories(Movement movement, HashMap<Long, String> incomeCategories,
            HashMap<Long, String> expenseCategories) {
        Long typeId = movement.getType().getId();
        if (Type.INCOME.get().equals(typeId)) {
            incomeCategories.put(movement.getCategory().getId(), movement.getCategory().getDescription());
        } else if (Type.EXPENSE.get().equals(typeId)) {
            expenseCategories.put(movement.getCategory().getId(), movement.getCategory().getDescription());
        }
    }

    private Summary initializeSummary() {
        return Summary.builder()
                .expenses(BigDecimal.ZERO)
                .incomes(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();
    }

    private void recalculateSummary(Summary summary, Movement movement) {
        if(movement.getType().getId().equals(Type.INCOME.get())) {
            summary.setIncomes(summary.getIncomes().add(movement.getValue()));
            summary.setTotal(summary.getTotal().add(movement.getValue()));
        }else {
            summary.setExpenses(summary.getExpenses().add(movement.getValue()));
            summary.setTotal(summary.getTotal().subtract(movement.getValue()));
        }
    }

    private PdfPCell getImageHeaderCell() {
        URL imageUrl = getClass().getResource(LOGO);
        Image image = PdfUtils.image(imageUrl);
        image.scalePercent(25);
        PdfPCell cell = new PdfPCell(image);
        PdfUtils.alignCellToCenter(cell);
        return cell;
    }

    private PdfPCell getNameHeaderCell(String title) {
        PdfPCell cell = new PdfPCell(new Paragraph(title, PdfUtils.HEAD_FONT));
        PdfUtils.alignCellToCenter(cell);
        return cell;
    }

    private PdfPCell headerCell(String name){
        return new PdfPCell(new Paragraph(name, PdfUtils.BODY_FONT));
    }

    private PdfPCell getDataHeaderCell(ReportInformation reportInformation){
        PdfPTable table = new PdfPTable(PdfUtils.ONE_COLUMN);

        table.addCell(headerCell("Fecha de generación: " + dateUtilities.getActualDate()));
        table.addCell(headerCell("Nombre: " + reportInformation.getUserName()));
        String dateMessage = String.format(DATE_MESSAGE_FILTER, reportInformation.getStartDate(), reportInformation.getEndDate());
        table.addCell(headerCell(dateMessage));
        table.addCell(headerCell(""));

        PdfPCell cell = new PdfPCell(table);
        cell.setBorderWidth(1);

        return cell;
    }

    private PdfPTable createHeader(String reportName, ReportInformation reportInformation) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.THREE_COLUMNS);

        table.addCell(getImageHeaderCell());
        table.addCell(getNameHeaderCell(reportName));
        table.addCell(getDataHeaderCell(reportInformation));
        table.setSpacingAfter(PdfUtils.SPACING_BEFORE_TEN);

        return table;
    }

    private PdfPTable createSummary(Summary summary) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.THREE_COLUMNS);

        table.addCell(headCell(INCOMES_TITLE));
        table.addCell(headCell(EXPENSES_TITLE));
        table.addCell(headCell(TOTAL_TITLE));

        table.addCell(bodyCell(formatValue(summary.getIncomes())));
        table.addCell(bodyCell(formatValue(summary.getExpenses())));
        table.addCell(bodyCell(formatValue(summary.getTotal())));

        table.setSpacingBefore(PdfUtils.SPACING_BEFORE_TEN);

        return table;
    }

    private PdfPTable printCharts(List<Movement> movements, HashMap<Long, String> incomeCategories,
            HashMap<Long, String> expenseCategories) {

        int columns = incomeCategories.isEmpty() || expenseCategories.isEmpty() ? PdfUtils.ONE_COLUMN
                : PdfUtils.TWO_COLUMN;

        PdfPTable table = PdfUtils.pdfTableFullWidth(columns);

        if (!incomeCategories.isEmpty()) {
            table.addCell(buildChartByCategory(movements, incomeCategories, INCOMES_TITLE));
        }
        if (!expenseCategories.isEmpty()) {
            table.addCell(buildChartByCategory(movements, expenseCategories, EXPENSES_TITLE));
        }

        table.setSpacingBefore(1);

        return table;
    }

    private PdfPCell buildChartByCategory(List<Movement> movements, HashMap<Long, String> categories, String title) {
        List<ChartSeries> chartSeries = generateChartSeries(movements, categories);
        byte[] graphicBytes = charts.bytes(charts.pie(chartSeries, title));
        PdfPCell cell = new PdfPCell(PdfUtils.image(graphicBytes));
        cell.setBorder(0);
        PdfUtils.alignCellToCenter(cell);
        return cell;
    }

    private List<ChartSeries> generateChartSeries(List<Movement> movements, HashMap<Long, String> categories) {
        List<ChartSeries> listChartSeries = new ArrayList<>();
        categories.entrySet().stream()
                .forEach(category -> listChartSeries.add(calculateTotalByCategory(movements, category)));
        return listChartSeries;
    }

    private ChartSeries calculateTotalByCategory(List<Movement> movements, Map.Entry<Long, String> category) {
        List<Movement> movementsByCategory = movements.stream()
                .filter(m -> m.getCategory().getId().intValue() == category.getKey().intValue())
                .collect(Collectors.toList());
        BigDecimal totalByCategory = movementsByCategory.stream().map(Movement::getValue).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        return charts.buildSerie(category.getValue(), totalByCategory);
    }
}