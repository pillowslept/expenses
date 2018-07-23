package co.com.expenses.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Params implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long movementId;
    private Long typeId;
    private Long categoryId;
    private BigDecimal value;
    private String observations;
    private String description;
    private String date;

}
