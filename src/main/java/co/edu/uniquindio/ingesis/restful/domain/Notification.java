package co.edu.uniquindio.ingesis.restful.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification extends PanacheEntity {
    @Size(min = 3, max = 50)
    private String message;
    private LocalDate sentDate;
    private boolean read;
    @NotNull
    private Long studentId;
}
