package co.edu.uniquindio.ingesis.restful.dtos.notifications;

public record NotificationDTO(
        String destinatario,
        String asunto,
        String mensaje
) {
}
