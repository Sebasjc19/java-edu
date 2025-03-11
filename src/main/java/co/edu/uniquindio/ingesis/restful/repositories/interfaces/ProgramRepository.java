package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.domain.Program;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface ProgramRepository extends PanacheRepository<Program> {
    List<Program> findByUserId(Long userId);
}
