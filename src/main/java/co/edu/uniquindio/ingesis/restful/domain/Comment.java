package co.edu.uniquindio.ingesis.restful.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment extends PanacheEntity {
    @NotBlank
    private String content;
    private LocalDate creationDate;
    @NotNull
    private Long professorId;
    @NotNull
    private Long programId;
}
