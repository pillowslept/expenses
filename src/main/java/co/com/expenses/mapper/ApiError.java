package co.com.expenses.mapper;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiError {

    private HttpStatus status;
    private int statusCode;
    private String message;
    private String debugMessage;

    ApiError(HttpStatus status, Throwable ex) {
        this.status = status;
        this.statusCode = status.value();
        this.message = ex.getMessage();
        this.debugMessage = ex.getLocalizedMessage();
    }

}
