package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.mappers.CommentMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.CommentRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.CommentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Inject
    CommentMapper commentMapper;
    final CommentRepository commentRepository;
}
