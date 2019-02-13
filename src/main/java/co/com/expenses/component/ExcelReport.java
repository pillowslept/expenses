package co.com.expenses.component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.expenses.dto.MovementSummary;
import co.com.expenses.dto.ReportInformation;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

@Component
public class ExcelReport {

    private static final Logger LOGGER = Logger.getLogger(ExcelReport.class.getName());
    private static final String ERROR_GENERATING_EXCEL = "Error generando excel";
    private static final String ERROR_READING_EXCEL = "Ocurrió un error intentando leer el archivo de excel";
    private static final String URL_EXCEL_RESOURCE = "/report/report.xls";
    private static final String DATE_MESSAGE_FILTER = "Desde: %s Hasta: %s";

    @Autowired
    DateUtilities dateUtilities;

    public ByteArrayInputStream generate(List<MovementSummary> movementsSummary, ReportInformation reportInformation) {
        InputStream stream = readExcelAsStream(URL_EXCEL_RESOURCE);
        ByteArrayOutputStream byteArrayOutput = replaceInformation(stream, movementsSummary, reportInformation);
        return convertToByteArrayInput(byteArrayOutput);
    }

    private ByteArrayInputStream convertToByteArrayInput(ByteArrayOutputStream byteArrayOutputStream) {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public InputStream readExcelAsStream(String reportName) {
        InputStream inputStream = null;
        URL reportUrl = getClass().getResource(reportName);
        try {
            inputStream = reportUrl.openStream();
        } catch (IOException e) {
            LOGGER.error(ERROR_READING_EXCEL, e);
        }
        return inputStream;
    }

    public ByteArrayOutputStream replaceInformation(InputStream report, List<MovementSummary> movementsSummary,
            ReportInformation reportInformation) {
        Map<String, Object> parameters = assignGeneralParametes(movementsSummary, reportInformation);
        return transformXLS(report, parameters);
    }

    private Map<String, Object> assignGeneralParametes(List<MovementSummary> movementsSummary, ReportInformation reportInformation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("generateDate", dateUtilities.getActualDate());
        parameters.put("userName", reportInformation.getUserName());
        String dateMessage = String.format(DATE_MESSAGE_FILTER, reportInformation.getStartDate(),
                reportInformation.getEndDate());
        parameters.put("filters", dateMessage);
        parameters.put("total", movementsSummary.size());
        parameters.put("movements", movementsSummary);
        return parameters;
    }

    private ByteArrayOutputStream transformXLS(InputStream report, Map<String, Object> parameters) {
        XLSTransformer transformer = new XLSTransformer();
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            HSSFWorkbook workbook = (HSSFWorkbook) transformer.transformXLS(report, parameters);
            byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            byteArrayOutputStream.close();
        } catch (IOException | ParsePropertyException | InvalidFormatException e) {
            LOGGER.error(ERROR_GENERATING_EXCEL, e);
        }
        return byteArrayOutputStream;
    }

}
