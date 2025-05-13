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
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
public class CommentServiceImplTest {

    @Inject
    CommentServiceImpl commentService;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    ProgramRepository programRepository;

    @InjectMock
    CommentRepository commentRepository;

    Logger auditLogger = Mockito.mock(Logger.class);

    @InjectMock
    CommentMapper commentMapper;



    @Test
    public void testCreateCommentSuccess() {
        CommentCreationRequest request = new CommentCreationRequest("Buen trabajo",LocalDate.now(), 1L, 2L);
        User mockUser = new User(); mockUser.id=1L;
        Program mockProgram = new Program(); mockProgram.id=2L;
        Comment mockComment = new Comment(); mockComment.setContent("Buen trabajo");
        mockComment.setProfessorId(1L);
        mockComment.setProgramId(2L);

        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.of(mockUser));
        when(programRepository.findByIdOptional(2L)).thenReturn(Optional.of(mockProgram));
        when(commentMapper.parseOf(request)).thenReturn(mockComment);
        when(commentMapper.toCommentResponse(mockComment)).thenReturn(new CommentResponse(1L, "Buen trabajo", LocalDate.now(),1L, 2L));

        CommentResponse result = commentService.createComment(request);

        assertNotNull(result);
        assertEquals("Buen trabajo", result.content());
    }

    @Test
    public void testCreateCommentProfesorNoExiste() {
        CommentCreationRequest request = new CommentCreationRequest("Mensaje", LocalDate.now(),99L,2L);

        when(userRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(request);
        });
    }
   /* @Test
    public void testGetAllCommentsSuccess() {
        int page = 0;
        List<Comment> mockComments = new ArrayList<>();
        Comment mockComment = new Comment();
        mockComment.setContent("Comentario de prueba");
        mockComments.add(mockComment);

        when(Comment.findAll().page(Page.of(page, 10)).list()).thenReturn(mockComments);
        when(commentMapper.toCommentResponse(mockComment)).thenReturn(new CommentResponse(1L, "Comentario de prueba", LocalDate.now(), 1L, 1L));

        List<CommentResponse> result = commentService.getAllComments(page);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Comentario de prueba", result.get(0).content());
        verify(auditLogger).info(contains("consulta todos los comentarios"), eq(page), eq(mockComments.size()));
    }*/
    @Test
    public void testGetCommentByIdSuccess() {
        Long commentId = 1L;
        Comment mockComment = new Comment();
        mockComment.setContent("Comentario de prueba");
        mockComment.setProfessorId(1L);
        mockComment.setProgramId(2L);

        when(commentRepository.findByIdOptional(commentId)).thenReturn(Optional.of(mockComment));
        when(commentMapper.toCommentResponse(mockComment)).thenReturn(new CommentResponse(1L, "Comentario de prueba", LocalDate.now(), 1L, 1L));

        CommentResponse result = commentService.getCommentById(commentId);

        assertNotNull(result);
        assertEquals("Comentario de prueba", result.content());
    }

    @Test
    public void testGetCommentByIdNotFound() {
        Long commentId = 1L;
        when(commentRepository.findByIdOptional(commentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentById(commentId);
        });
    }
    @Test
    public void testUpdateCommentByIdSuccess() {
        Long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest("Comentario actualizado");
        Comment mockComment = new Comment();
        mockComment.setContent("Comentario de prueba");
        mockComment.setProfessorId(1L);
        mockComment.setProgramId(2L);

        when(commentRepository.findByIdOptional(commentId)).thenReturn(Optional.of(mockComment));
        when(commentMapper.toCommentResponse(mockComment)).thenReturn(new CommentResponse(1L, "Comentario actualizado", LocalDate.now(), 1L, 1L));

        CommentResponse result = commentService.updateCommentById(commentId, request);

        assertNotNull(result);
        assertEquals("Comentario actualizado", result.content());
    }

    @Test
    public void testUpdateCommentByIdNotFound() {
        Long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest("Comentario actualizado");
        when(commentRepository.findByIdOptional(commentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.updateCommentById(commentId, request);
        });
    }
    @Test
    public void testDeleteCommentSuccess() {
        Long commentId = 1L;

        Comment mockComment = Mockito.spy(new Comment());
        mockComment.setContent("Comentario de prueba");
        mockComment.setProfessorId(1L);
        mockComment.setProgramId(2L);

        when(commentRepository.findByIdOptional(commentId)).thenReturn(Optional.of(mockComment));
        doNothing().when(mockComment).delete();

        CommentResponse result = commentService.deleteComment(commentId);

        assertNotNull(result);
        assertEquals("Comentario de prueba", result.content());
    }


    @Test
    public void testDeleteCommentNotFound() {
        Long commentId = 1L;
        when(commentRepository.findByIdOptional(commentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.deleteComment(commentId);
        });
    }
    @Test
    public void testFindCommentsByProfessorIdSuccess() {
        Long professorId = 1L;
        int page = 0;
        List<Comment> mockComments = new ArrayList<>();
        Comment mockComment = new Comment();
        mockComment.setContent("Comentario de prueba");
        mockComment.setProfessorId(1L);
        mockComment.setProgramId(2L);
        mockComments.add(mockComment);

        when(userRepository.findByIdOptional(professorId)).thenReturn(Optional.of(new User()));
        when(commentRepository.findByProfessorId(professorId)).thenReturn(mockComments);
        when(commentMapper.toCommentResponse(mockComment)).thenReturn(new CommentResponse(1L, "Comentario de prueba", LocalDate.now(), professorId, 2L));

        List<CommentResponse> result = commentService.findCommentsByProfessorId(professorId, page);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Comentario de prueba", result.get(0).content());
    }

    @Test
    public void testFindCommentsByProfessorIdNotFound() {
        Long professorId = 1L;
        int page = 0;

        when(userRepository.findByIdOptional(professorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.findCommentsByProfessorId(professorId, page);
        });
    }

}
