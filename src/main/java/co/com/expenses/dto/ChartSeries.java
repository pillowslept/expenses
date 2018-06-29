package co.com.expenses.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    private Number value;
    private String name;

}
