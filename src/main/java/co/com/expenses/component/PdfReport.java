package co.com.expenses.component;

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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.com.expenses.model.Movement;
import co.com.expenses.util.DateUtilities;

@Component
public class PdfReport {

    private static final String ERROR_WHEN_OBTAIN_IMAGE = "Ocurrió un error obteniendo el logo para el PDF, de la ruta <%s>.";
    private static final String ERROR_GENERATING_PDF = "Ocurrió un error en la generación del PDF";
    private static final String REPORT_NAME = "REPORTE DE MOVIMIENTOS";
    private static final int TABLE_100_PERCENT = 100;
    private static final String LOGO = "file:///C://Users//ceiba//Downloads//expenses.jpg";
    private static final int FONT_SIZE_BODY = 8;
    private static final int FONT_SIZE_HEAD = 10;
    private static final Font HEAD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, FONT_SIZE_HEAD);
    private static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_BODY);

    private static final Logger LOGGER = Logger.getLogger(PdfReport.class.getName());

    public ByteArrayInputStream generate(List<Movement> movements) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(TABLE_100_PERCENT);
            table.setWidths(new int[]{1, 4, 2, 2, 2});

            table.addCell(headCell("Id"));
            table.addCell(headCell("F. creación"));
            table.addCell(headCell("Valor"));
            table.addCell(headCell("Tipo"));
            table.addCell(headCell("Categoría"));

            for (Movement movement : movements) {
                table.addCell(bodyCell(movement.getId().toString()));
                table.addCell(bodyCell(DateUtilities.timestampToString(movement.getCreationDate())));
                table.addCell(bodyCell(movement.getValue().toString()));
                table.addCell(bodyCell(movement.getType().getDescription()));
                table.addCell(bodyCell(movement.getCategory().getDescription()));
            }

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(createHeader(REPORT_NAME));
            document.add(table);
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
            image.scalePercent(20);
        } catch (IOException | BadElementException e) {
            LOGGER.info(String.format(ERROR_WHEN_OBTAIN_IMAGE, LOGO), e);
        }
        PdfPCell cell = new PdfPCell(image);
        alignCellToCenter(cell);
        return cell;
    }

    private void alignCellToCenter(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(1);
    }

    private PdfPCell getNameHeaderCell(String title) {
        PdfPCell cell = new PdfPCell(new Paragraph(title, HEAD_FONT));
        alignCellToCenter(cell);
        return cell;
    }

    private PdfPCell headerCell(String name){
        return new PdfPCell(new Paragraph(name, BODY_FONT));
    }

    private PdfPCell getDataHeaderCell(){
        PdfPTable table = new PdfPTable(1);

        table.addCell(headerCell("F. de generación: " + DateUtilities.getActualDate()));
        table.addCell(headerCell("Nombre: " + "dsajdksajdksaj dksajdksa"));
        table.addCell(headerCell("Email: " + "juan djsdksjdks"));
        table.addCell(headerCell(""));

        PdfPCell cell = new PdfPCell(table);
        cell.setBorderWidth(1);

        return cell;
    }

    private PdfPTable createHeader(String reportName) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(TABLE_100_PERCENT);

        table.addCell(getImageHeaderCell());
        table.addCell(getNameHeaderCell(reportName));
        table.addCell(getDataHeaderCell());
        table.setSpacingAfter(10);

        return table;
    }

    private PdfPCell headCell(String name){
        PdfPCell hcell = new PdfPCell(new Phrase(name, HEAD_FONT));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return hcell;
    }

    private PdfPCell bodyCell(String attribute){
        PdfPCell cell = new PdfPCell(new Phrase(attribute, BODY_FONT));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}