package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.UserMapper;
import co.edu.uniquindio.ingesis.restful.services.interfaces.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Inject  UserMapper userMapper;

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        // Lógica para crear un usuario
        User user = userMapper.parseOf(request);
        user.persist();

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = User.findById(id);
        if( user == null ){
            new ResourceNotFoundException();
        }
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUsuarioById(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new NotFoundException("Usuario no encontrado con ID: " + id);
        }
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse deleteUsuario(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new NotFoundException("Usuario no encontrado con ID: " + id);
        }
        user.delete();
        return userMapper.toUserResponse(user);
    }
}
