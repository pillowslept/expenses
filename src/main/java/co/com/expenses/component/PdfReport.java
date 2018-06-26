package co.com.expenses.component;

import static co.com.expenses.util.PdfUtils.bodyCell;
import static co.com.expenses.util.PdfUtils.headCell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.com.expenses.model.Movement;
import co.com.expenses.util.DateUtilities;
import co.com.expenses.util.PdfUtils;

@Component
public class PdfReport {

    private static final String ERROR_WHEN_OBTAIN_IMAGE = "Ocurrió un error obteniendo el logo para el PDF, de la ruta <%s>.";
    private static final String ERROR_GENERATING_PDF = "Ocurrió un error en la generación del PDF";
    private static final String REPORT_NAME = "REPORTE DE MOVIMIENTOS";

    private static final String LOGO = "file:///C://Users//ceiba//Downloads//expenses.jpg";

    private static final Logger LOGGER = Logger.getLogger(PdfReport.class.getName());

    public ByteArrayInputStream generate(List<Movement> movements) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = PdfUtils.pdfTableFullWidth(6);
            table.setWidths(new int[]{1, 1, 4, 2, 1, 1});

            table.addCell(headCell("Id"));
            table.addCell(headCell("Valor"));
            table.addCell(headCell("Observaciones"));
            table.addCell(headCell("Fecha"));
            table.addCell(headCell("Tipo"));
            table.addCell(headCell("Categoría"));

            for (Movement movement : movements) {
                table.addCell(bodyCell(movement.getId().toString()));
                table.addCell(bodyCell(movement.getValue().toString()));
                table.addCell(bodyCell(movement.getObservations()));
                table.addCell(bodyCell(DateUtilities.timestampToString(movement.getCreationDate())));
                table.addCell(bodyCell(movement.getType().getDescription()));
                table.addCell(bodyCell(movement.getCategory().getDescription()));
            }

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(createHeader(REPORT_NAME));
            document.add(table);
            document.add(createFooter());
            document.close();
        } catch (DocumentException ex) {
            LOGGER.error(String.format(ERROR_GENERATING_PDF), ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPCell getImageHeaderCell() {
        Image image = null;
        try {
            image = Image.getInstance(new URL(LOGO));
            image.scalePercent(25);
        } catch (IOException | BadElementException e) {
            LOGGER.info(String.format(ERROR_WHEN_OBTAIN_IMAGE, LOGO), e);
        }
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
        PdfPTable table = new PdfPTable(1);

        table.addCell(headerCell("F. de generación: " + DateUtilities.getActualDate()));
        table.addCell(headerCell("Nombre: " + "Juan Camilo Velásquez"));
        table.addCell(headerCell("Fecha: " + "12/12/2018 a 12/12/2019"));
        table.addCell(headerCell(""));

        PdfPCell cell = new PdfPCell(table);
        cell.setBorderWidth(1);

        return cell;
    }

    private PdfPTable createHeader(String reportName) {
        PdfPTable table = PdfUtils.pdfTableFullWidth(3);

        table.addCell(getImageHeaderCell());
        table.addCell(getNameHeaderCell(reportName));
        table.addCell(getDataHeaderCell());
        table.setSpacingAfter(10);

        return table;
    }

    private PdfPTable createFooter() {
        PdfPTable table = PdfUtils.pdfTableFullWidth(3);

        table.addCell(headCell("Ingresos"));
        table.addCell(headCell("Egresos"));
        table.addCell(headCell("Total"));

        table.addCell(bodyCell("50000"));
        table.addCell(bodyCell("150000"));
        table.addCell(bodyCell("100000"));

        table.setSpacingBefore(10);

        return table;
    }

}