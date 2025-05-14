package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.comments.CommentResponse;
import co.edu.uniquindio.ingesis.restful.dtos.comments.UpdateCommentRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.CommentMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.CommentRepository;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ProgramRepository;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.CommentService;
import co.edu.uniquindio.ingesis.restful.utils.ErrorMessages;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Inject
    CommentMapper commentMapper;
    @Inject
    CommentRepository commentRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    ProgramRepository programRepository;
    private static final Logger auditLogger = LoggerFactory.getLogger("audit");

    @Override
    public List<CommentResponse> getAllComments(int page) {
        // Se obtienen todos los comentarios  de la base de datos, sin importar su estado lógico
        List<Comment> comments = PanacheEntity.findAll().page(Page.of(page, 10)).list();

        auditLogger.info("consulta todos los comentarios, página '{}', total='{}'",
                 page, comments.size());

        // Convertir la lista de entidades en una lista de respuestas DTO
        return comments.stream().
                map(commentMapper::toCommentResponse)
                .collect( Collectors.toList());
        }

    @Override
    public CommentResponse getCommentById(Long id) {
        Optional<Comment>optionalComment = commentRepository.findByIdOptional(id);
        if (optionalComment.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.COMMENT_NOT_FOUND);        }
        Comment comment = optionalComment.get();
        if(comment.getProfessorId() == null){
            throw new ResourceNotFoundException("Profesor no encontrado");}

        auditLogger.info("consulta comentario: id='{}'", id);

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse createComment(CommentCreationRequest request) {
        Optional<User>userOptional = userRepository.findByIdOptional(request.professorId());
        Optional<Program>programOptional = programRepository.findByIdOptional(request.programId());
        if (userOptional.isEmpty()){
            throw new ResourceNotFoundException("Profesor no encontrado");
        }
        if (programOptional.isEmpty()){
            throw new ResourceNotFoundException("Program no encontrado");
        }
        Comment comment = commentMapper.parseOf(request);
        comment.setCreationDate(LocalDate.now());
        comment.persist();

        auditLogger.info("Creación comentario: profesorId='{}', programaId='{}'",
                 request.professorId(), request.programId());

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse updateCommentById(Long id, UpdateCommentRequest request) {

        // Validar si el comentario se encuentra en la base de datos
        Optional<Comment> optionalComment = commentRepository.findByIdOptional(id);
        if (optionalComment.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.COMMENT_NOT_FOUND);
        }

        Comment comment = optionalComment.get();
        comment.setContent(request.content());

        auditLogger.info("actualización comentario: id='{}'", id);

        // Convertir entidad en DTO de respuesta
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse deleteComment(Long id) {
        // Validar si el comentario se encuentra en la base de datos
        Optional<Comment> optionalComment = commentRepository.findByIdOptional(id);
        if (optionalComment.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.COMMENT_NOT_FOUND);
        }

        // Obtener el comentario y eliminarlo
        Comment comment = optionalComment.get();
        comment.delete();

        auditLogger.info("Eliminación comentario: id='{}'", id);

        return commentMapper.toCommentResponse(comment);
    }
    //TODO: preguntar si es correcto retornar una lista de responses
    @Override
    public List<CommentResponse> findCommentsByProfessorId(Long professorId, int page) {
        Optional<User> userOptional = userRepository.findByIdOptional(professorId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("El profesor no se encuentra registrado");
        }

        // Obtener todos los comentarios
        List<Comment> allComments = commentRepository.findByProfessorId(professorId);

        int size = 10; // Tamaño fijo de página
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allComments.size());

        if (fromIndex >= allComments.size()) {
            return Collections.emptyList();
        }

        List<Comment> paginatedComments = allComments.subList(fromIndex, toIndex);

        auditLogger.info("Consulta comentarios del profesor: profesorId='{}', página='{}', total='{}'",
                 professorId, page, paginatedComments.size());

        return paginatedComments.stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }



}
