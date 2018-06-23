package co.com.expenses.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "CATEGORY")
@Data
@Builder
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Size(max = 10)
    @Column(name = "STATE", nullable = false)
    private String state;

}
