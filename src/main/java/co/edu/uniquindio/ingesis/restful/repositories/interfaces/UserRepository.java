package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PanacheRepository<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> getActiveUsers();
}
