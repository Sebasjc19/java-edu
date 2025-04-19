package co.edu.uniquindio.ingesis.restful.producers;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class NotificacionProducer {
    @Channel("notificaciones")
    Emitter<String> emmiter;

    public void sendNotificacion(String notificacion) {
        emmiter.send(notificacion);
    }
}
