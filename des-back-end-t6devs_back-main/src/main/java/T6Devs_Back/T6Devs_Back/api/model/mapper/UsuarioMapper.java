package T6Devs_Back.T6Devs_Back.api.model.mapper;

import org.mapstruct.Mapper;

import javax.annotation.processing.Generated;

import org.springframework.stereotype.Component;

import T6Devs_Back.T6Devs_Back.api.dto.UsuarioDTO;
import T6Devs_Back.T6Devs_Back.api.model.entity.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // Converte Entidade → DTO
    UsuarioDTO toDto(Usuario usuario);

    // Converte DTO → Entidade
    Usuario toEntity(UsuarioDTO usuarioDTO);
}