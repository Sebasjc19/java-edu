package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.usuarios.ShowUserRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserRegistrationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserResponse;
import co.edu.uniquindio.ingesis.restful.dtos.usuarios.UserUpdateRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRegistrationRequest request);
    UserResponse findById(Long id) throws ResourceNotFoundException;
    void deleteUser(Long id);
    UserResponse showUser(Long id, ShowUserRequest showUserRequest);
    List<UserResponse> getActiveUsers();
    List<UserResponse> getAllUsers(int page);
    UserResponse updateUserById(Long id, UserUpdateRequest request);
}
