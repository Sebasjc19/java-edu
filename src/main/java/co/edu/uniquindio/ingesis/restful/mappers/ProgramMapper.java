package co.edu.uniquindio.ingesis.restful.mappers;

import co.edu.uniquindio.ingesis.restful.domain.Program;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ProgramMapper {

    Program parseOf(ProgramCreationRequest request);
    ProgramResponse toProgramResponse(Program program);

}
