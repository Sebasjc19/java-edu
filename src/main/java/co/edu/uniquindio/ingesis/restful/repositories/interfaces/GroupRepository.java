package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.domain.Group;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface GroupRepository extends PanacheRepository<Group> {
    List<Group> findByProfessorId(Long professorId);
}
