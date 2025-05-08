package T6Devs_Back.T6Devs_Back.api.dto;

import T6Devs_Back.T6Devs_Back.api.model.entity.TipoUsuario;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private boolean ativo;
}