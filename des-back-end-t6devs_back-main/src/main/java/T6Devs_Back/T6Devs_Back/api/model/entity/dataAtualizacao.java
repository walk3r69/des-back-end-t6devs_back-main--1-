package T6Devs_Back.T6Devs_Back.api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class dataAtualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "laboratory_id")
    private Laboratory laboratory;

    public dataAtualizacao() {
        this.lastUpdated = LocalDateTime.now(); // Inicializa com a data atual
    }
}