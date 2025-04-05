package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Status;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {
    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> userOptional = find("email", email).stream().findAny();
        return userOptional;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> optionalUser = find("username", username).firstResultOptional();
        return optionalUser;
    }

    @Override
    public List<User> getActiveUsers() {
        return list("status", Status.ACTIVE);
    }
}
