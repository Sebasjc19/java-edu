package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.domain.Role;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.groups.AddStudentToGroupRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.UpdateGroupRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.groups.implementations.IdNotCreatedException;
import co.edu.uniquindio.ingesis.restful.exceptions.groups.implementations.StudentNotInGroupException;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.RoleNotAllowedException;
import co.edu.uniquindio.ingesis.restful.mappers.GroupMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.GroupRepository;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.UserRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.GroupService;
import co.edu.uniquindio.ingesis.restful.utils.ErrorMessages;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.logging.ErrorManager;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    @Inject
    GroupMapper groupMapper;
    @Inject
    GroupRepository groupRepository;

    private static final Logger auditLogger = LoggerFactory.getLogger("audit");

    @Override
    public GroupResponse findGroupByProfessorId(Long professorId) {
        User tutor = User.findById(professorId);
        if(tutor == null){
            throw new ResourceNotFoundException("El usuario con rol de profesor no existe");
        }
        if(tutor.getRole() != Role.TUTOR){
            throw new RoleNotAllowedException("Este rol no tiene permitida esa accion");
        }
        Optional<Group> group = groupRepository.findByProfessorId(professorId);
        if (group.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.GROUP_NOT_FOUND);
        }
        Group groupEntity = group.get();
        auditLogger.info("Consulta de grupos por profesor: professorId='{}', total='{}'",
                professorId, groupEntity.getName());

        return groupMapper.toGroupResponse(groupEntity);
    }

    @Override
    public GroupResponse getGroupById(Long id) {
        Group group = groupRepository.findById(id);
        if( group == null ){
            throw new ResourceNotFoundException(ErrorMessages.GROUP_NOT_FOUND);
        }

        auditLogger.info("Consulta de grupo por ID: groupId='{}'",
                id);

        return groupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional
    public GroupResponse createGroup(GroupCreationRequest request) {
        User tutor = User.findById(request.idProfessor());
        if(tutor==null){
            throw new ResourceNotFoundException("El usuario con rol de profesor no existe");
        }
        if(tutor.getRole() != Role.TUTOR){
            throw new RoleNotAllowedException("El profesor asignado no tiene un rol valido");
        }
        Group group = groupMapper.parseOf(request);
        group.persist();
        if(group.id != null){
            tutor.setGroupId(group.id);
            tutor.persist();
        }else {
            throw new IdNotCreatedException("El Id del grupo no fue generado");
        }

        auditLogger.info("Grupo creado: nombre='{}', profesorId='{}'",
                request.name(), request.idProfessor());

        return groupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional
    public GroupResponse updateGroupById(Long id, UpdateGroupRequest request) {
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(id);
        if (optionalGroup.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.GROUP_NOT_FOUND);
        }
        User tutor = User.findById(request.idProfessor());
        if (tutor == null) {
            throw new ResourceNotFoundException("El profesor asignado no existe");
        }
        if(tutor.getRole() != Role.TUTOR){
            throw new RoleNotAllowedException("El profesor asignado no tiene un rol valido");
        }

        Group group = optionalGroup.get();
        group.setName(request.name());
        group.setTutorId(tutor.id);
        tutor.setGroupId(group.id);
        tutor.persist();
        group.persist();

        auditLogger.info("Grupo actualizado: id='{}', nuevoNombre='{}', nuevoProfesorId='{}'",
                id, request.name(), request.idProfessor());

        return groupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional
    public GroupResponse deleteGroup(Long id) {
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(id);
        if (optionalGroup.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.GROUP_NOT_FOUND);
        }

        Group group = optionalGroup.get();
        User tutor = User.findById(group.getTutorId());
        if(tutor != null) {
            tutor.setGroupId(null);
            tutor.persist();
        }else {
            throw new ResourceNotFoundException("El profesor asignado ya no existe");
        }
        for(Long student: group.getStudentsIds()){
            User studentUser = User.findById(student);
            if(studentUser != null) {
                studentUser.setGroupId(null);
                studentUser.persist();
            }else {
                throw new ResourceNotFoundException("El estudiante miembro ya no existe");
            }
        }
        group.delete();

        auditLogger.info("Grupo eliminado: id='{}'",
                id);

        return groupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional
    public void addStudentToGroup(Long groupId, AddStudentToGroupRequest request) {
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(groupId);
        if (optionalGroup.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.GROUP_NOT_FOUND);
        }
        User studentUser = User.findById(request.studentId());
        if (studentUser == null) {
            throw new ResourceNotFoundException("El estudiante a añadir no existe");
        }
        Group group = optionalGroup.get();
        group.getStudentsIds().add(studentUser.id);
        group.persist();
    }

    @Override
    @Transactional
    public void removeStudentFromGroup(Long groupId, Long studentId) {
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(groupId);
        if (optionalGroup.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.GROUP_NOT_FOUND);
        }
        User studentUser = User.findById(studentId);
        if (studentUser == null) {
            throw new ResourceNotFoundException("El estudiante miembro ya no existe");
        }
        Group group = optionalGroup.get();
        if(!group.getStudentsIds().contains(studentUser.id)){
            throw new StudentNotInGroupException("El estudiante a eliminar no hace parte del grupo");
        }
        group.getStudentsIds().remove(studentUser.id);
        studentUser.setGroupId(null);
        studentUser.persist();
        group.persist();
    }


}
