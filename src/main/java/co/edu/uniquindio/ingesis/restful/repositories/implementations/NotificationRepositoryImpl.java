package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Notification;
import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.NotificationRepository;

import java.util.List;

public class NotificationRepositoryImpl implements NotificationRepository {
    @Override
    public List<Notification> findNotificationsByStudentId(Long studentId) {
        return list("studentId", studentId);
    }
}
