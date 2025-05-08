package T6Devs_Back.T6Devs_Back.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SoftwareDTO {

    private Long id;
    private String nome;
    private String link;
    private String versao;
    private boolean disponivel;
    private TipoSoftware tipo;
    private LocalDate dataSolicitacao;
    private List<Long> laboratoriosIds; 
    
    public enum TipoSoftware {
        LIVRE, PROPRIETARIO
    }
}