package co.edu.uniquindio.ingesis.restful.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group extends PanacheEntity {
    @Size(min = 3, max = 20)
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "group_student_ids",
            joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "student_user_id")
    private Set<Long> studentsIds = new HashSet<>();

    private Long tutorId;
}
