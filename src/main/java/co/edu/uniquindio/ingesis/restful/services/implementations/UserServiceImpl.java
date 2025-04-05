package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Status;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ShowUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations.EmailAlreadyExistsException;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations.InactiveUserException;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.implementations.UsernameAlreadyExistsException;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.mappers.InactiveUserExceptionMapper;
import co.edu.uniquindio.ingesis.restful.mappers.UserMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.UserService;
import io.quarkus.panache.common.Page;
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
public class UserServiceImpl implements UserService {
    @Inject  UserMapper userMapper;
    @Inject
    UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        User user = userMapper.parseOf(request);

        Optional<User> optionalUser = userRepository.findByEmail(request.email());
        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("El correo '"+ request.email() + "' ya esta registrado");
        }

        Optional<User> optionalUser1 = userRepository.findByUsername(request.username());
        if(optionalUser1.isPresent()){
            throw new UsernameAlreadyExistsException("El username '"+request.username()+"' ya esta registrado");
        }
        user.setRegistrationDate(LocalDate.now());
        user.setStatus(Status.ACTIVE);
        user.persist();

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findById(Long id) throws ResourceNotFoundException {
        User user = User.findById(id);
        if( user == null ){
            throw new ResourceNotFoundException("Usuario no encontrado");
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
    public List<UserResponse> getAllUsers(int page) {

        // Se obtienen todos los usarios de la base de datos, sin importar su estado lógico
        List<User> users = User.findAll().page(Page.of(page, 10)).list();

        if(users.isEmpty()){
            throw new ResourceNotFoundException("No se encontraron usuarios registrados");
        }

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
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        /*
         Se lanza una excepción en caso de que se intente actualizar un usuario inactivo
         */
        if(optionalUser.get().getStatus() == Status.INACTIVE){
            throw new InactiveUserException("El usuario esta inactivo");
        }

        User user = optionalUser.get();
        user.setUsername(String.valueOf(request.username()));
        user.setEmail(String.valueOf(request.email()));
        user.setPassword(String.valueOf(request.password()));

        user.persist();

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        // Validar si el usuario se encuentra en la base de datos
        Optional<User> optionalUser = userRepository.findByIdOptional(id);
        if (optionalUser.isEmpty()) {
            new ResourceNotFoundException("Usuario no encontrado");
        }

        // Obtener el usuario y cambiar su estado logico a inactivo
        User user = optionalUser.get();
        user.setStatus(Status.INACTIVE);
        user.persist();
    }

    @Override
    public UserResponse showUser(Long id, ShowUserRequest showUserRequest) {
        return null;
    }
}
