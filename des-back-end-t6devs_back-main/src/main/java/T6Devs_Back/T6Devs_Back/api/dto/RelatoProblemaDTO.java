package T6Devs_Back.T6Devs_Back.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatoProblemaDTO {
    private Long id;
    private Long solicitacaoId;
    private String descricao;
    private LocalDate dataRelato;
}