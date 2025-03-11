package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.GroupRepository;

import java.util.List;

public class GroupRepositoryImpl implements GroupRepository {
    @Override
    public List<Group> findByProfessorId(Long professorId) {
        return list("professor.id", professorId);
    }
}
