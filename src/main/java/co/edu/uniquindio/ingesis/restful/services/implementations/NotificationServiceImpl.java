package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Notification;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.NotificationMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.NotificationRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Inject
    NotificationMapper notificationMapper;
    final NotificationRepository notificationRepository;


    @Override
    public List<NotificationResponse> findNotificationsByStudentId(Long studentId) {
        // Se buscan las notificaciones en base al id del estudiante en la base de datos
        List<Notification> notifications = notificationRepository
                .findNotificationsByStudentId(studentId);

        // Convertir la lista de entidades en una lista de respuestas DTO
        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .collect( Collectors.toList());
    }

    @Override
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = Notification.findById(id);
        if( notification == null ){
            new ResourceNotFoundException();
        }
        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public NotificationResponse createNotification(NotificationCreationRequest request) {
        Notification notification = notificationMapper.parseOf(request);
        notification.setSentDate(LocalDate.now());
        notification.setRead(false);
        notification.persist();

        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public NotificationResponse deleteNotification(Long id) {
        // Validar si la notificación a borrar se encuentra en la base de datos
        Optional<Notification> optionalNotification = notificationRepository.findByIdOptional(id);
        if (optionalNotification.isEmpty()) {
            new ResourceNotFoundException();
        }

        // Obtener la notificación y eliminarlo
        Notification notification = optionalNotification.get();
        notification.delete();

        return notificationMapper.toNotificationResponse(notification);
    }
}
