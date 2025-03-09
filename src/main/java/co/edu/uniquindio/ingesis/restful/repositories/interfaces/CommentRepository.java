package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface CommentRepository extends PanacheRepository<Comment> {
}
