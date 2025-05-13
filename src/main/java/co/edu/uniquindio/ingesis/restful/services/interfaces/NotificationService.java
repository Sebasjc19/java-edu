package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.UpdateGroupRequest;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.notifications.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> findNotificationsByStudentId(Long studentId,int page);
    NotificationResponse getNotificationById(Long id);
    void sendNotification(NotificationCreationRequest request);
}
