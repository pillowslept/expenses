package co.com.expenses.component;

import static java.awt.Color.WHITE;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.expenses.dto.ChartSeries;

@Component
public class Charts {

    private static final int HEIGHT = 165;
    private static final int WIDTH = 265;
    private static final Logger LOGGER = LogManager.getLogger(PdfReport.class.getName());

    @Autowired
    Messages messages;

    public PieChart pie(List<ChartSeries> listChartSeries, String title) {

        PieChart chart = new PieChartBuilder().width(WIDTH).height(HEIGHT).title(title).build();

        listChartSeries.stream().forEach(ch -> chart.addSeries(ch.getName(), ch.getValue()));

        chart.getStyler().setChartBackgroundColor(WHITE);
        chart.getStyler().setLegendBorderColor(WHITE);
        chart.getStyler().setPlotBorderColor(WHITE);
        chart.getStyler().setLegendPadding(2);

        return chart;
    }

    public byte[] bytes(PieChart chart) {
        byte[] bytes = new byte[0];
        try {
            bytes = BitmapEncoder.getBitmapBytes(chart, BitmapFormat.PNG);
        } catch (IOException e) {
            LOGGER.error(messages.get("charts.error.bytes"), e);
        }
        return bytes;
    }

    public ChartSeries buildSerie(String name, Number value) {
        return ChartSeries.builder().value(value).name(name).build();
    }

}
