package co.com.expenses.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object data;
    private String message;

}
