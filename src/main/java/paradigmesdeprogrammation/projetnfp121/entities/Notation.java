package paradigmesdeprogrammation.projetnfp121.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "notation")
public class Notation {
    @EmbeddedId
    private NotationId id;

    @MapsId("iddevoir")
    @ManyToOne(fetch = FetchType. EAGER)
    @JoinColumn(name = "iddevoir", nullable = false)
    private Devoir iddevoir;

    @MapsId("idetudiant")
    @ManyToOne(fetch = FetchType. EAGER)
    @JoinColumn(name = "idetudiant", nullable = false)
    private Etudiant idetudiant;

    @Column(precision = 4, scale = 2)
    private BigDecimal note;

}