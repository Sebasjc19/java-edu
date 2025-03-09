package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface CommentRepository extends PanacheRepository<Comment> {
    List<Comment> findByProfessorId(Long professorId);
}
