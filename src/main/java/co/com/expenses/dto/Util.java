package co.com.expenses.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Util implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private String state;
    private String createdAt;
    private String updatedAt;

}
