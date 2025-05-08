package T6Devs_Back.T6Devs_Back.api.dto;

import lombok.Data;

@Data
public class ProfessorDTO {
    private Long id;
    private String nome;
    private String escola;
    private String email;
}