package co.edu.uniquindio.ingesis.restful.mappers;

import co.edu.uniquindio.ingesis.restful.domain.Group;
import co.edu.uniquindio.ingesis.restful.domain.User;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.groups.GroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface GroupMapper {

    Group parseOf(GroupCreationRequest request);
    GroupResponse toGroupResponse(Group group);

}
