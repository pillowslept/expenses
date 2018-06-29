package co.com.expenses.component;

import static co.com.expenses.util.CurrencyUtilities.formatValue;
import static co.com.expenses.util.PdfUtils.bodyCell;
import static co.com.expenses.util.PdfUtils.headCell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import co.com.expenses.model.Movement;
import co.com.expenses.util.DateUtilities;
import co.com.expenses.util.PdfUtils;

@Component
public class PdfReport {

    private static final String ERROR_GENERATING_PDF = "Ocurrió un error en la generación del PDF";
    private static final String REPORT_NAME = "REPORTE DE MOVIMIENTOS";

    private static final String LOGO = "file:///C://Users//ceiba//Downloads//expenses.jpg";

    private static final Logger LOGGER = Logger.getLogger(PdfReport.class.getName());

    @Autowired
    Charts charts;
    
    public ByteArrayInputStream generate(List<Movement> movements) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.SIX_COLUMNS);
            table.setWidths(new int[]{1, 1, 4, 2, 1, 1});

            table.addCell(headCell("Id"));
            table.addCell(headCell("Valor"));
            table.addCell(headCell("Observaciones"));
            table.addCell(headCell("Fecha"));
            table.addCell(headCell("Tipo"));
            table.addCell(headCell("Categoría"));

            Resume resume = initializeResume();

            for (Movement movement : movements) {
                table.addCell(bodyCell(movement.getId().toString()));
                table.addCell(bodyCell(formatValue(movement.getValue())));
                table.addCell(bodyCell(movement.getObservations()));
                table.addCell(bodyCell(DateUtilities.timestampToString(movement.getCreationDate())));
                table.addCell(bodyCell(movement.getType().getDescription()));
                table.addCell(bodyCell(movement.getCategory().getDescription()));
                recalculateResume(resume, movement);
            }

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(createHeader(REPORT_NAME));
            document.add(table);
            document.add(createResume(resume));
            document.add(printCharts(resume));
            document.close();
        } catch (DocumentException ex) {
            LOGGER.error(String.format(ERROR_GENERATING_PDF), ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private Resume initializeResume() {
        return Resume.builder()
                .expenses(BigDecimal.ZERO)
                .incomes(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();
    }

    private void recalculateResume(Resume resume, Movement movement) {
        if(movement.getType().getId().equals(1L)) {
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
        table.setSpacingAfter(10);

        return table;
    }

    private PdfPTable createResume(Resume resume) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.THREE_COLUMNS);

        table.addCell(headCell("Ingresos (I)"));
        table.addCell(headCell("Egresos (E)"));
        table.addCell(headCell("Total (T)"));

        table.addCell(bodyCell(formatValue(resume.getIncomes())));
        table.addCell(bodyCell(formatValue(resume.getExpenses())));
        table.addCell(bodyCell(formatValue(resume.getTotal())));

        table.setSpacingBefore(10);

        return table;
    }

    private PdfPTable printCharts(Resume resume) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(PdfUtils.THREE_COLUMNS);

        byte[] graphicBytes = charts.bytes(charts.pie(generateChartSeries()));
        PdfPCell cell = new PdfPCell(PdfUtils.image(graphicBytes));
        cell.setBorder(0);
        cell.setColspan(3);
        PdfUtils.alignCellToCenter(cell);
        table.addCell(cell);

        table.setSpacingBefore(1);

        return table;
    }

    private List<ChartSeries> generateChartSeries() {
        List<ChartSeries> listChartSeries = new ArrayList<>();
        listChartSeries.add(charts.buildSerie("Ropa", 25000));
        listChartSeries.add(charts.buildSerie("Comida", 10000));
        listChartSeries.add(charts.buildSerie("Gasolina", 15000));
        listChartSeries.add(charts.buildSerie("Copper", 25000));
        listChartSeries.add(charts.buildSerie("Zinc", 25000));
        return listChartSeries;
    }
}