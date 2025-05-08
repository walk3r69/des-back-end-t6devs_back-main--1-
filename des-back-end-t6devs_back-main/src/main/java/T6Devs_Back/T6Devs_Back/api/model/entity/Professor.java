package T6Devs_Back.T6Devs_Back.api.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo; // Referencia ao enum definido em TipoUsuario.java
}