package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Group;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends PanacheRepository<Group> {
    Optional<Group> findByProfessorId(Long professorId);
}
