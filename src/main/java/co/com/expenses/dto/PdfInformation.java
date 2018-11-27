package co.com.expenses.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PdfInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String startDate;
    private String endDate;
    

}
