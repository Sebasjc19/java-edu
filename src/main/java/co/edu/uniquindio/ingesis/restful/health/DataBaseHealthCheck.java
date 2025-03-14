package co.edu.uniquindio.ingesis.restful.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DataBaseHealthCheck implements HealthCheck {
    @Inject
    EntityManager entityManager; // Inyecta el EntityManager

    @Override
    public HealthCheckResponse call() {
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult(); // Ejecuta consulta de prueba
            return HealthCheckResponse.up("Base de datos conectada");
        } catch (PersistenceException e) {
            return HealthCheckResponse.down("Error en la conexión a la BD: " + e.getMessage());
        }
    }
}
