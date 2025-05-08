package T6Devs_Back.T6Devs_Back.api.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Software {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String link; // Adicionado para suportar o mapeamento de link

    private String versao;

    private boolean disponivel;

    private boolean livre; // Adicionado para suportar o mapeamento de tipo (LIVRE/PROPRIETARIO)

    private String dataSolicitacao; // Adicionado para suportar o mapeamento de dataSolicitacao

    @ManyToMany(mappedBy = "softwaresInstalados")
    private List<Laboratory> laboratorios;
}