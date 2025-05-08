package T6Devs_Back.T6Devs_Back.api.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String status;

    @ManyToMany
    @JoinTable(
        name = "laboratory_software",
        joinColumns = @JoinColumn(name = "laboratory_id"),
        inverseJoinColumns = @JoinColumn(name = "software_id")
    )
    private List<Software> softwaresInstalados;
}