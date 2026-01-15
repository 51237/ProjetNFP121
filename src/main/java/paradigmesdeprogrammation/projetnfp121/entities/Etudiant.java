package paradigmesdeprogrammation.projetnfp121.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

//    private String photo;

    @ManyToOne(fetch = FetchType. EAGER)
    @JoinColumn(name = "idclasse", nullable = true)
    private Classe idclasse;
}
