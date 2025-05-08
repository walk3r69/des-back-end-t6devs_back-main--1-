package T6Devs_Back.T6Devs_Back.api.model.mapper;

import org.mapstruct.Mapper;
import T6Devs_Back.T6Devs_Back.api.dto.ProfessorDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Professor;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {
    ProfessorDTO toDto(Professor professor);
    Professor toEntity(ProfessorDTO dto);
}