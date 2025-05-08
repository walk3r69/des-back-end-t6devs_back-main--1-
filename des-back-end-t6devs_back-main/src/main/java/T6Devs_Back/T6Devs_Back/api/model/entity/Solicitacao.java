package T6Devs_Back.T6Devs_Back.api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "software_id")
    private Software software;

    @ManyToOne
    @JoinColumn(name = "laboratory_id")
    private Laboratory laboratory;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private LocalDate dataUso;
    private LocalTime horarioUso;
    private String status;

    private static final List<String> VALID_STATUSES = Arrays.asList("PENDENTE", "APROVADA", "FINALIZADA");

    public void setStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Status inválido: deve ser PENDENTE, APROVADA ou FINALIZADA");
        }
        this.status = status;
    }
}