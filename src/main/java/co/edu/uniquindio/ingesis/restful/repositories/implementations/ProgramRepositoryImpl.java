package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ProgramRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProgramRepositoryImpl implements ProgramRepository {
    @Override
    public List<Program> findByUserId(Long userId) {
        return list("userId", userId);
    }
}
