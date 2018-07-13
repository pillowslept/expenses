package co.com.expenses.component;

import static co.com.expenses.util.CurrencyUtilities.formatValue;
import static co.com.expenses.util.PdfUtils.bodyCell;
import static co.com.expenses.util.PdfUtils.headCell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
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
import co.com.expenses.dto.Resume;
import co.com.expenses.enums.Type;
import co.com.expenses.model.Movement;
import co.com.expenses.service.CategoryService;
import co.com.expenses.util.DateUtilities;
import co.com.expenses.util.PdfUtils;

@Component
public class PdfReport {

    private static final String TOTAL_TITLE = "Total";
    private static final String EXPENSES_TITLE = "Egresos";
    private static final String INCOMES_TITLE = "Ingresos";
    private static final String ERROR_GENERATING_PDF = "Ocurrió un error en la generación del PDF";
    private static final String REPORT_NAME = "REPORTE DE MOVIMIENTOS";

    private static final String LOGO = "file:///C://Users//ceiba//Downloads//expenses.jpg";

    private static final Logger LOGGER = Logger.getLogger(PdfReport.class.getName());

    @Autowired
    Charts charts;

    @Autowired
    CategoryService categoryService;

    public ByteArrayInputStream generate(List<Movement> movements) {
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
            

            Resume resume = initializeResume();
            HashMap<Long, String> incomeCategories = new HashMap<>();
            HashMap<Long, String> expenseCategories = new HashMap<>();

            for (Movement movement : movements) {
                table.addCell(bodyCell(movement.getType().getDescription()));
                table.addCell(bodyCell(movement.getCategory().getDescription()));
                table.addCell(bodyCell(formatValue(movement.getValue())));
                table.addCell(bodyCell(movement.getObservations()));
                table.addCell(bodyCell(DateUtilities.timestampToString(movement.getCreationDate())));

                recalculateResume(resume, movement);
                validateCategories(movement, incomeCategories, expenseCategories);
            }

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(createHeader(REPORT_NAME));
            document.add(table);
            document.add(createResume(resume));
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

    private Resume initializeResume() {
        return Resume.builder()
                .expenses(BigDecimal.ZERO)
                .incomes(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();
    }

    private void recalculateResume(Resume resume, Movement movement) {
        if(movement.getType().getId().equals(Type.INCOME.get())) {
            resume.setIncomes(resume.getIncomes().add(movement.getValue()));
            resume.setTotal(resume.getTotal().add(movement.getValue()));
        }else {
            resume.setExpenses(resume.getExpenses().add(movement.getValue()));
            resume.setTotal(resume.getTotal().subtract(movement.getValue()));
        }
    }

    private PdfPCell getImageHeaderCell() {
        Image image = PdfUtils.image(LOGO);
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

    private PdfPCell getDataHeaderCell(){
        PdfPTable table = new PdfPTable(PdfUtils.ONE_COLUMN);

        table.addCell(headerCell("F. de generación: " + DateUtilities.getActualDate()));
        table.addCell(headerCell("Nombre: " + "Juan Camilo Velásquez"));
        table.addCell(headerCell("Fecha: " + "12/12/2018 a 12/12/2019"));
        table.addCell(headerCell(""));

        PdfPCell cell = new PdfPCell(table);
        cell.setBorderWidth(1);

        return cell;
    }

    private PdfPTable createHeader(String reportName) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.THREE_COLUMNS);

        table.addCell(getImageHeaderCell());
        table.addCell(getNameHeaderCell(reportName));
        table.addCell(getDataHeaderCell());
        table.setSpacingAfter(PdfUtils.SPACING_BEFORE_TEN);

        return table;
    }

    private PdfPTable createResume(Resume resume) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.THREE_COLUMNS);

        table.addCell(headCell(INCOMES_TITLE));
        table.addCell(headCell(EXPENSES_TITLE));
        table.addCell(headCell(TOTAL_TITLE));

        table.addCell(bodyCell(formatValue(resume.getIncomes())));
        table.addCell(bodyCell(formatValue(resume.getExpenses())));
        table.addCell(bodyCell(formatValue(resume.getTotal())));

        table.setSpacingBefore(PdfUtils.SPACING_BEFORE_TEN);

        return table;
    }

    private PdfPTable printCharts(List<Movement> movements, HashMap<Long, String> incomeCategories,
            HashMap<Long, String> expenseCategories) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(2);

        table.addCell(buildChartByCategory(movements, incomeCategories, INCOMES_TITLE));
        table.addCell(buildChartByCategory(movements, expenseCategories, EXPENSES_TITLE));

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