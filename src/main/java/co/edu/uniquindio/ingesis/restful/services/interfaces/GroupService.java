package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.groups.AddStudentToGroupRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.UpdateGroupRequest;

import java.util.List;

public interface GroupService {
    GroupResponse findGroupByProfessorId(Long professorId);
    GroupResponse getGroupById(Long id);
    GroupResponse createGroup(GroupCreationRequest request);
    GroupResponse updateGroupById(Long id, UpdateGroupRequest request);
    GroupResponse deleteGroup(Long id);
    void addStudentToGroup(Long groupId, AddStudentToGroupRequest request);
    void removeStudentFromGroup(Long groupId, Long studentId);
}
