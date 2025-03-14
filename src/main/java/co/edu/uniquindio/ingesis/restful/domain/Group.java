package co.edu.uniquindio.ingesis.restful.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group extends PanacheEntity {
    @Size(min = 3, max = 20)
    private String name;
    @NotNull
    private String idProfessor;
}
