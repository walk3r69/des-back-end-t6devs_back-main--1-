package T6Devs_Back.T6Devs_Back.api.model.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import T6Devs_Back.T6Devs_Back.api.dto.SoftwareDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Laboratory;
import T6Devs_Back.T6Devs_Back.api.model.entity.Software;

@Mapper(componentModel = "spring", uses = { LaboratoryMapper.class })
public interface SoftwareMapper {

    @Mapping(target = "tipo", expression = "java(software.isLivre() ? SoftwareDTO.TipoSoftware.LIVRE : SoftwareDTO.TipoSoftware.PROPRIETARIO)")
    @Mapping(target = "dataSolicitacao", expression = "java(mapStringToLocalDate(software.getDataSolicitacao()))")
    @Mapping(target = "laboratoriosIds", expression = "java(software.getLaboratorios().stream().map(lab -> lab.getId()).toList())")
    @Mapping(target = "link", source = "link") // Adicionado mapeamento explícito para link
    SoftwareDTO toDto(Software software);

    @Mapping(target = "livre", expression = "java(dto.getTipo() == SoftwareDTO.TipoSoftware.LIVRE)")
    @Mapping(target = "dataSolicitacao", expression = "java(mapLocalDateToString(dto.getDataSolicitacao()))")
    @Mapping(target = "laboratorios", ignore = true) // Ignora o mapeamento direto
    @Mapping(target = "link", source = "link") // mapeamento explícito para link
    Software toEntity(SoftwareDTO dto);

    default LocalDate mapStringToLocalDate(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return LocalDate.parse(data);
    }

    default String mapLocalDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    default Long mapLaboratoryToId(Laboratory laboratory) {
        return laboratory != null ? laboratory.getId() : null;
    }
}