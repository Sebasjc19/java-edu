package co.edu.uniquindio.ingesis.restful.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "programs")
public class Program extends PanacheEntity {
    @NotNull
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String code;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    @NotNull
    private Type type;
    @NotNull
    private Long userId;
}
