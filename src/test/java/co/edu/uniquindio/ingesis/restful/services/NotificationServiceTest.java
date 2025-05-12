package co.edu.uniquindio.ingesis.restful.services;

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
import co.edu.uniquindio.ingesis.restful.services.implementations.NotificationServiceImpl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import java.util.Optional;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class NotificationServiceTest {

    @Inject
    NotificationServiceImpl notificationService;

    @InjectMock
    NotificationRepository notificationRepository;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    NotificationMapper notificationMapper;

    Logger auditLogger = Mockito.mock(Logger.class);


    @InjectMock
    NotificationProducer producer;

    @Test
    void getNotificationById() {
        Long id = 1L;
        Notification notification = new Notification();
        notification.id = id;

        when(notificationRepository.findByIdOptional(id))
                .thenReturn(Optional.of(notification));

        when(notificationMapper.toNotificationResponse(notification))
                .thenReturn(new NotificationResponse(id, "msg", LocalDate.now(), false, 2L));

        NotificationResponse result = notificationService.getNotificationById(id);

        assertNotNull(result);
    }

    @Test
    void getNotificationByIdNoEncontrada() {
        Long id = 1L;
        when(notificationRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.getNotificationById(id);
        });
    }

    @Test
    void sendNotification_usuarioExiste() {
        Long studentId = 1L;
        User user = new User();
        user.setEmail("test@email.com");

        NotificationCreationRequest request = new NotificationCreationRequest("mensaje", studentId);

        when(userRepository.findByIdOptional(studentId)).thenReturn(Optional.of(user));

        notificationService.sendNotification(request);

        ArgumentCaptor<NotificationDTO> captor = ArgumentCaptor.forClass(NotificationDTO.class);
        verify(producer).sendNotificacion(captor.capture());

        NotificationDTO sentDto = captor.getValue();
        assertEquals(user.getEmail(), sentDto.destinatario());
        assertEquals("mensaje", sentDto.mensaje());
    }

    @Test
    void sendNotificationUsuarioNoExiste() {
        Long studentId = 1L;
        NotificationCreationRequest request = new NotificationCreationRequest("mensaje", studentId);

        when(userRepository.findByIdOptional(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.sendNotification(request);
        });

        verify(producer, never()).sendNotificacion(any());
    }

}
