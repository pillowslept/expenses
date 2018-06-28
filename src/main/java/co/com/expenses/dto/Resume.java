package co.com.expenses.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal incomes;
    private BigDecimal expenses;
    private BigDecimal total;

}
