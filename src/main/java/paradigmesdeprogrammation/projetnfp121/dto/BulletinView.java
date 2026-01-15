package paradigmesdeprogrammation.projetnfp121.dto;

import java.math.BigDecimal;
import java.util.Map;

public class BulletinView {

    private Long idEtudiant;
    private String nom;
    private String prenom;

    private Map<String, BigDecimal> moyenneParMatiere;

    private BigDecimal moyenneGenerale;

    public BulletinView(Long idEtudiant, String nom, String prenom,
                        Map<String, BigDecimal> moyenneParMatiere,
                        BigDecimal moyenneGenerale) {
        this.idEtudiant = idEtudiant;
        this.nom = nom;
        this.prenom = prenom;
        this.moyenneParMatiere = moyenneParMatiere;
        this.moyenneGenerale = moyenneGenerale;
    }

    public Long getIdEtudiant() { return idEtudiant; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public Map<String, BigDecimal> getMoyenneParMatiere() { return moyenneParMatiere; }
    public BigDecimal getMoyenneGenerale() { return moyenneGenerale; }
}

