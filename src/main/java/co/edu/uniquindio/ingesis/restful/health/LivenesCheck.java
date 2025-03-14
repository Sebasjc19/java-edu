package co.edu.uniquindio.ingesis.restful.health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import jakarta.enterprise.context.ApplicationScoped;

@Liveness // Indica que es un chequeo de "vida" del servicio
@ApplicationScoped
public class LivenesCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("Servicio en ejecución");
    }
}
