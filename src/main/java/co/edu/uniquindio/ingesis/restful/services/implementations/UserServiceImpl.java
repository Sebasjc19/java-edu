package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Status;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.EmailAlreadyExistsException;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.InactiveUserException;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.UsernameAlreadyExistsException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Inject
    UserMapper userMapper;
    @Inject
    UserRepository userRepository;
    @Inject
    SendEmailServiceImpl sendEmailService;

    private static final Logger auditLogger = LoggerFactory.getLogger("audit");

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        User user = userMapper.parseOf(request);

        Optional<User> optionalUser = userRepository.findByEmail(request.email());
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyExistsException("El correo '" + request.email() + "' ya esta registrado");
        }

        Optional<User> optionalUser1 = userRepository.findByUsername(request.username());
        if (optionalUser1.isPresent()) {
            throw new UsernameAlreadyExistsException("El username '" + request.username() + "' ya esta registrado");
        }
        user.setRegistrationDate(LocalDate.now());
        user.setStatus(Status.ACTIVE);
        user.persist();
        // Después de guardar, enviar correo de bienvenida
        String asunto = "¡Bienvenido a la plataforma!";
        String mensaje = "Hola " + user.getUsername() + ",\n\n" +
                "Gracias por registrarte. ¡Nos alegra tenerte con nosotros!";

        //sendEmailService.enviarCorreo(user.getEmail(), asunto, mensaje);


        auditLogger.info("Usuario creado: username='{}', email='{}'", request.username(), request.email());

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findById(Long id) throws ResourceNotFoundException {
        User user = User.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        auditLogger.info("Consulta de usuario por ID: id='{}'", id);

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getActiveUsers() {

        // Se obtienen solo los usuarios activos en la base de datos
        List<User> users = userRepository.getActiveUsers();

        auditLogger.info("Consulta de usuarios activos. Total encontrados: {}", users.size());

        // Convertir la lista de entidades en una lista de respuestas DTO
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllUsers(int page) {

        // Se obtienen todos los usarios de la base de datos, sin importar su estado lógico
        List<User> users = User.findAll().page(Page.of(page, 10)).list();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron usuarios registrados");
        }

        auditLogger.info("Consulta de todos los usuarios. Página: {}, Total encontrados: {}", page, users.size());


        // Convertir la lista de entidades en una lista de respuestas DTO
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
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
        if (optionalUser.get().getStatus() == Status.INACTIVE) {
            throw new InactiveUserException("El usuario esta inactivo");
        }


        User user = optionalUser.get();
        if (request.username().isPresent()) {
            user.setUsername(request.username().get());
        }
        if (request.email().isPresent()) {
            user.setEmail(request.email().get());
        }
        if (request.password().isPresent()) {
            user.setPassword(request.password().get());
        }

        user.persist();

        auditLogger.info("Usuario actualizado: id='{}', nuevo username='{}', nuevo email='{}'", id, request.username(), request.email());

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        // Validar si el usuario se encuentra en la base de datos
        Optional<User> optionalUser = userRepository.findByIdOptional(id);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontradi");
        }

        auditLogger.info("Usuario desactivado (borrado lógico): id='{}'", id);


        // Obtener el usuario y cambiar su estado logico a inactivo
        User user = optionalUser.get();
        user.setStatus(Status.INACTIVE);
        user.persist();
    }
}
