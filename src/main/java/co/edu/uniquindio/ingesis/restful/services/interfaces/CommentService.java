package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentResponse;

public interface CommentService {
    CommentResponse getAllComments();
    CommentResponse getCommentById(Long id);
    CommentResponse createComment();
    CommentResponse updateCommentById(Long id, CommentCreationRequest comment);
    CommentResponse deleteComment(Long id);
    CommentResponse findCommentsByProfessorId(Long professorId);
}
