package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentResponse;
import co.edu.uniquindio.ingesis.restful.dtos.comments.UpdateCommentRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.CommentMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.CommentRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.CommentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Inject
    CommentMapper commentMapper;
    CommentRepository commentRepository;

    @Override
    public CommentResponse getAllComments() {
        return null;
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        Comment comment = Comment.findById(id);
        if( comment == null ){
            new ResourceNotFoundException("Comentario no encontrado");
        }
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse createComment(CommentCreationRequest request) {
        Comment comment = commentMapper.parseOf(request);
        comment.setCreationDate(LocalDate.now());
        comment.persist();

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse updateCommentById(Long id, UpdateCommentRequest request) {

        // Validar si el comentario se encuentra en la base de datos
        Optional<Comment> optionalComment = commentRepository.findByIdOptional(id);
        if (optionalComment.isEmpty()) {
            new ResourceNotFoundException("Comentario no encontrado");
        }

        Comment comment = optionalComment.get();
        comment.setContent(request.content());

        // Convertir entidad en DTO de respuesta
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse deleteComment(Long id) {
        // Validar si el comentario se encuentra en la base de datos
        Optional<Comment> optionalComment = commentRepository.findByIdOptional(id);
        if (optionalComment.isEmpty()) {
            new ResourceNotFoundException("Comentario no encontrado");
        }

        // Obtener el comentario y eliminarlo
        Comment comment = optionalComment.get();
        comment.delete();

        return commentMapper.toCommentResponse(comment);
    }

    //TODO: preguntar si es correcto retornar una lista de responses
    @Override
    public List<CommentResponse> findCommentsByProfessorId(Long professorId) {

        // Buscar los comentarios del profesor en la base de datos
        List<Comment> comments = commentRepository.findByProfessorId(professorId);

        // Convertir la lista de entidades en una lista de respuestas DTO
        return comments.stream()
                .map(commentMapper::toCommentResponse)
                .collect( Collectors.toList());
    }

}
