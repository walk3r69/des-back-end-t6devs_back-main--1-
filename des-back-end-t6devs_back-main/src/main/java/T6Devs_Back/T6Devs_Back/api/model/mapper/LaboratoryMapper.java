package T6Devs_Back.T6Devs_Back.api.model.mapper;

import T6Devs_Back.T6Devs_Back.api.dto.LaboratoryDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Laboratory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LaboratoryMapper {

    @Mapping(target = "softwaresInstaladosIds", expression = "java(laboratory.getSoftwaresInstalados().stream().map(T6Devs_Back.T6Devs_Back.api.model.entity.Software::getId).toList())")
    LaboratoryDTO toDto(Laboratory laboratory);

    Laboratory toEntity(LaboratoryDTO dto);
}