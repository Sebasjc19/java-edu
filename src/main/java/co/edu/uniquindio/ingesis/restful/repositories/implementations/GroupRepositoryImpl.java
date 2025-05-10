package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.GroupRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GroupRepositoryImpl implements GroupRepository {
    @Override
    public Optional<Group> findByProfessorId(Long professorId) {
        return find("tutorId", professorId).stream().findFirst();
    }
}
