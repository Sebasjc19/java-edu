package co.edu.uniquindio.ingesis.restful.producers;

import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationCreationRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.notifications.implementations.NotificationSendException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class NotificationProducer {
    @Channel("notificaciones")
    Emitter<String> emitter;

    public void sendNotificacion(NotificationCreationRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(request);
            emitter.send(json);
        } catch (JsonProcessingException e) {
            throw new NotificationSendException("Error al serializar la notificación a JSON");
        }
    }
}
