package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.CommentRepository;

import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {
    @Override
    public List<Comment> findByProfessorId(Long professorId) {
        // Encuentra todos los comentarios donde professor.id coinciden con el parámetro
        return list("professor.id", professorId);
    }
}
