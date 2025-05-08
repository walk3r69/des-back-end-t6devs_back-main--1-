package T6Devs_Back.T6Devs_Back.api.model.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String senhaHash;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
    
    private boolean ativo = true;
    
    @CreationTimestamp
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}