package org.acme.getting.started.ejericicio4;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "favorito",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tipo", "referenciaId"})
)
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String referenciaId;

    @NotBlank
    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;
}
