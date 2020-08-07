package co.com.expenses.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PageableSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalElements;
    private Long totalPages;
    private Long numberOfElements;
    private List<MovementSummary> content;

}
