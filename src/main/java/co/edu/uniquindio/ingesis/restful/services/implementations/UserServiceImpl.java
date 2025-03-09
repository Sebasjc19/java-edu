package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.domain.Status;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.EmailAlredyExistsExceptionMapper;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.InactiveUserExceptionMapper;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.UserMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Inject  UserMapper userMapper;
    final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        User user = userMapper.parseOf(request);
        Optional<User> optionalUser = userRepository.finByEmail(request.email());
        if(optionalUser.isPresent()){
            new EmailAlredyExistsExceptionMapper();
        }
        user.setRegistrationDate(LocalDate.now());
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
    public List<UserResponse> getActiveUsers() {

        // Se obtienen solo los usuarios activos en la base de datos
        List<User> users = userRepository.getActiveUsers();

        // Convertir la lista de entidades en una lista de respuestas DTO
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect( Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllUsers() {

        // Se todos los usarios de la base de datos, sin importar su estado lógico
        List<User> users = User.listAll();

        // Convertir la lista de entidades en una lista de respuestas DTO
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect( Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUserById(Long id, UserUpdateRequest request) {

        // Validar si el usuario se encuentra en la base de datos
        Optional<User> optionalUser = userRepository.findByIdOptional(id);
        if (optionalUser.isEmpty()) {
            new ResourceNotFoundException();
        }

        /*
         Se lanza una excepción en caso de que se intente actualizar un usuario inactivo
         */
        if(optionalUser.get().getStatus() == Status.INACTIVE){
            new InactiveUserExceptionMapper();
        }

        User user = optionalUser.get();
        user.setUsername(String.valueOf(request.username()));
        user.setEmail(String.valueOf(request.email()));
        user.setPassword(String.valueOf(request.password()));

        user.persist();

        // Convertir entidad en DTO de respuesta
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse deleteUser(Long id) {

        // Validar si el usuario se encuentra en la base de datos
        Optional<User> optionalUser = userRepository.findByIdOptional(id);
        if (optionalUser.isEmpty()) {
            new ResourceNotFoundException();
        }

        // Obtener el usuario y cambiar su estado logico a inactivo
        User user = optionalUser.get();
        user.setStatus(Status.INACTIVE);
        user.persist();

        return userMapper.toUserResponse(user);
    }
}
