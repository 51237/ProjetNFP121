package paradigmesdeprogrammation.projetnfp121.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Devoir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String categorie;

    @ManyToOne(fetch = FetchType. EAGER)
    @JoinColumn(name = "idclasse", nullable = false)
    private Classe idclasse;

    @ManyToOne(fetch = FetchType. EAGER)
    @JoinColumn(name = "idmatiere", nullable = false)
    private Matiere idmatiere;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal coefficient;

    @Column(nullable = false)
    private LocalDate dateDeCreation;

}