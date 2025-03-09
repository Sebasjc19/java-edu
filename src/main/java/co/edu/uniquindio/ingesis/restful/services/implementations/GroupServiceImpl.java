package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import co.edu.uniquindio.ingesis.restful.dtos.groups.UpdateGroupRequest;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.GroupMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.GroupRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.GroupService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    @Inject
    GroupMapper groupMapper;
    final GroupRepository groupRepository;

    @Override
    public List<GroupResponse> findGroupsByProfessorId(Long professorId) {

        // Buscar los grupos del profesor en la base de datos
        List<Group> groups = groupRepository.findByProfessorId(professorId);

        // Convertir la lista de entidades en una lista de respuestas DTO
        return groups.stream()
                .map(groupMapper::toGroupResponse)
                .collect( Collectors.toList());
    }

    @Override
    public GroupResponse getGroupById(Long id) {
        Group group = Group.findById(id);
        if( group == null ){
            new ResourceNotFoundException();
        }
        return groupMapper.toGroupResponse(group);
    }

    @Override
    public GroupResponse createGroup(GroupCreationRequest request) {
        Group group = groupMapper.parseOf(request);
        group.persist();

        return groupMapper.toGroupResponse(group);
    }

    @Override
    public GroupResponse updateGroupById(Long id, UpdateGroupRequest request) {

        // Validar si el grupo se encuentra en la base de datos
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(id);
        if (optionalGroup.isEmpty()) {
            new ResourceNotFoundException();
        }

        Group group = optionalGroup.get();
        group.setName(request.name());
        group.setIdProfessor(request.idProfessor());
        group.persist();

        // Convertir entidad en DTO de respuesta
        return groupMapper.toGroupResponse(group);
    }

    @Override
    public GroupResponse deleteGroup(Long id) {
        // Validar si el grupo se encuentra en la base de datos
        Optional<Group> optionalGroup = groupRepository.findByIdOptional(id);
        if (optionalGroup.isEmpty()) {
            new ResourceNotFoundException();
        }

        // Obtener el grupo y eliminarlo
        Group group = optionalGroup.get();
        group.delete();

        return groupMapper.toGroupResponse(group);
    }
}
