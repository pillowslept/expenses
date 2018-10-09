package co.com.expenses.util;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfUtils {

    private static final Logger LOGGER = Logger.getLogger(PdfUtils.class.getName());

    public static final int TABLE_100_PERCENT = 100;

    private static final int FONT_SIZE_BODY = 8;
    private static final int FONT_SIZE_HEAD = 10;

    public static final int THREE_COLUMNS = 3;
    public static final int ONE_COLUMN = 1;
    public static final int FIVE_COLUMNS = 5;
    public static final int SIX_COLUMNS = 6;

    public static final int SPACING_BEFORE_TEN = 10;

    public static final Font HEAD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, FONT_SIZE_HEAD);
    public static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, FONT_SIZE_BODY);

    private PdfUtils() {
    }

    public static PdfPCell headCell(String name) {
        PdfPCell hcell = new PdfPCell(new Phrase(name, HEAD_FONT));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return hcell;
    }

    public static PdfPCell bodyCell(String attribute) {
        PdfPCell cell = new PdfPCell(new Phrase(attribute, BODY_FONT));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    public static PdfPTable pdfTableFullWidth(int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(PdfUtils.TABLE_100_PERCENT);
        return table;
    }

    public static void alignCellToCenter(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(1);
    }

    public static Image image(byte[] bytes) {
        Image image = null;
        try {
            image = Image.getInstance(bytes);
        } catch (BadElementException | IOException e) {
            LOGGER.info("Ocurrió un error obteniendo la imagen a partir del array de bytes", e);
        }
        return image;
    }

    public static Image image(URL url) {
        Image image = null;
        try {
            image = Image.getInstance(url);
        } catch (BadElementException | IOException e) {
            LOGGER.info(String.format("Ocurrió un error obteniendo la imagen a partir de la URL %s", url), e);
        }
        return image;
    }
}
