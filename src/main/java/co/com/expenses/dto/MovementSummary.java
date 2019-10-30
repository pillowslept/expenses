package co.com.expenses.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class MovementSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Timestamp creationDate;
    private BigDecimal value;
    private String observations;
    private CategorySummary category;
    private TypeSummary type;

}
