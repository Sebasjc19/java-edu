package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentResponse;
import co.edu.uniquindio.ingesis.restful.dtos.comments.UpdateCommentRequest;

import java.util.List;

public interface CommentService {
    CommentResponse getAllComments();
    CommentResponse getCommentById(Long id);
    CommentResponse createComment(CommentCreationRequest request);
    CommentResponse updateCommentById(Long id, UpdateCommentRequest comment);
    CommentResponse deleteComment(Long id);
    List<CommentResponse> findCommentsByProfessorId(Long professorId);
}
