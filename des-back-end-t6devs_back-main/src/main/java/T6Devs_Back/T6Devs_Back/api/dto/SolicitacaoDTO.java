package T6Devs_Back.T6Devs_Back.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoDTO {
    private Long id;
    private Long softwareId;
    private Long laboratoryId;
    private Long professorId;
    private LocalDate dataUso;
    private LocalTime horarioUso;
    private String status;
}