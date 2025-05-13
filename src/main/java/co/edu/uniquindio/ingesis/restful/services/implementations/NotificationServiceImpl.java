package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.domain.Notification;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationDTO;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.NotificationMapper;
import co.edu.uniquindio.ingesis.restful.producers.NotificationProducer;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.NotificationRepository;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwk.Use;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Inject
    NotificationMapper notificationMapper;
    @Inject
    NotificationRepository notificationRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    NotificationProducer notificationProducer;

    private static final Logger auditLogger = LoggerFactory.getLogger("audit");

    @Override
    public List<NotificationResponse> findNotificationsByStudentId(Long studentId, int page) {
        // Se buscan las notificaciones en base al id del estudiante en la base de datos
        List<Notification> notifications = notificationRepository
                .findNotificationsByStudentId(studentId);
        int size = 10; // Tamaño fijo de página
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, notifications.size());

        if (fromIndex >= notifications.size()) {
            return Collections.emptyList();
        }

        List<Notification> paginatedNotifications = notifications.subList(fromIndex, toIndex);

        auditLogger.info("Consulta de notificaciones: studentId='{}', page='{}', resultados='{}'",
                studentId, page, paginatedNotifications.size());

        // Convertir la lista de entidades en una lista de respuestas DTO
        return paginatedNotifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .collect( Collectors.toList());
    }

    @Override
    public NotificationResponse getNotificationById(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findByIdOptional(id);
        if(optionalNotification.isEmpty()) {
            throw  new ResourceNotFoundException("Notificacion no encontrada");
        }
        Notification notification = optionalNotification.get();

        auditLogger.info("Consulta de notificación por ID", id);

        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public void sendNotification(NotificationCreationRequest request) {
        Optional<User> useOptional = userRepository.findByIdOptional(request.studentId());
        if(useOptional.isEmpty()){
            throw new ResourceNotFoundException("No se encuentra un usuario");
        }
        User user = useOptional.get();
        NotificationDTO notificationDTO = new NotificationDTO(user.getEmail(),"Creacion de cuenta",request.message(), request.studentId());
        notificationProducer.sendNotificacion(notificationDTO);

        //Notification notification = notificationMapper.parseOf(request);
        //notification.setSentDate(LocalDate.now());
        //notification.setRead(false);
        //notification.persist();

        //auditLogger.info("Notificación creada: destinatarioId='{}', fecha='{}'"
        //        , request.studentId(), notification.getSentDate());

        //return notificationMapper.toNotificationResponse(notification);
    }

}
