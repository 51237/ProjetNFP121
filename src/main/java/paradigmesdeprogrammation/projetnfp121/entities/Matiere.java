package paradigmesdeprogrammation.projetnfp121.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String denomination;

}