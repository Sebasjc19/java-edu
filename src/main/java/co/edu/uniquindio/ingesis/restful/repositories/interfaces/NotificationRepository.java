package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Notification;
import co.edu.uniquindio.ingesis.restful.domain.Program;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface NotificationRepository extends PanacheRepository<Notification> {
    List<Notification> findNotificationsByStudentId(Long studentId);
}
