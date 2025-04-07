package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.UpdateGroupRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.GroupMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.GroupRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.GroupService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    @Inject
    GroupMapper groupMapper;
    GroupRepository groupRepository;

    private static final Logger auditLogger = LoggerFactory.getLogger("audit");

    @Override
    public List<GroupResponse> findGroupsByProfessorId(Long professorId) {

        // Buscar los grupos del profesor en la base de datos
        List<Group> groups = groupRepository.findByProfessorId(professorId);

        auditLogger.info("Consulta de grupos por profesor: professorId='{}', total='{}'",
                professorId, groups.size());

        // Convertir la lista de entidades en una lista de respuestas DTO
        return groups.stream()
                .map(groupMapper::toGroupResponse)
                .collect( Collectors.toList());
    }

    @Override
    public GroupResponse getGroupById(Long id) {
        Group group = Group.findById(id);
        if( group == null ){
            new ResourceNotFoundException("Grupo no encontrado");
        }

        auditLogger.info("Consulta de grupo por ID: groupId='{}'",
                id);

        return groupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional
    public GroupResponse createGroup(GroupCreationRequest request) {
        Group group = groupMapper.parseOf(request);
        group.persist();

        auditLogger.info("Grupo creado: nombre='{}', profesorId='{}'",
                request.name(), request.idProfessor());

        return groupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional
    public GroupResponse updateGroupById(Long id, UpdateGroupRequest request) {

        // Validar si el grupo se encuentra en la base de datos
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(id);
        if (optionalGroup.isEmpty()) {
            new ResourceNotFoundException("Grupo no encontrado");
        }

        Group group = optionalGroup.get();
        group.setName(request.name());
        group.setIdProfessor(request.idProfessor());
        group.persist();

        auditLogger.info("Grupo actualizado: id='{}', nuevoNombre='{}', nuevoProfesorId='{}'",
                id, request.name(), request.idProfessor());

        // Convertir entidad en DTO de respuesta
        return groupMapper.toGroupResponse(group);
    }

    @Override
    @Transactional
    public GroupResponse deleteGroup(Long id) {
        // Validar si el grupo se encuentra en la base de datos
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(id);
        if (optionalGroup.isEmpty()) {
            new ResourceNotFoundException("Grupo no encontrado");
        }

        // Obtener el grupo y eliminarlo
        Group group = optionalGroup.get();
        group.delete();

        auditLogger.info("Grupo eliminado: id='{}'",
                id);

        return groupMapper.toGroupResponse(group);
    }
}
