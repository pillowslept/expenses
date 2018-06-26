package co.com.expenses.util;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfUtils {

    public static final int TABLE_100_PERCENT = 100;

    private static final int FONT_SIZE_BODY = 8;
    private static final int FONT_SIZE_HEAD = 10;

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
}
