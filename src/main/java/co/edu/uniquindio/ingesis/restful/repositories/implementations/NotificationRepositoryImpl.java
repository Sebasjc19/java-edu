package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Notification;
import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class NotificationRepositoryImpl implements NotificationRepository {
    @Override
    public List<Notification> findNotificationsByStudentId(Long studentId) {
        return list("studentId", studentId);
    }
}
