package paradigmesdeprogrammation.projetnfp121.dto;

import java.math.BigDecimal;

public class NoteView {
    private Long idEtudiant;
    private String nom;
    private String prenom;
    private BigDecimal note; // peut être null

    public NoteView(Long idEtudiant, String nom, String prenom, BigDecimal note) {
        this.idEtudiant = idEtudiant;
        this.nom = nom;
        this.prenom = prenom;
        this.note = note;
    }

    public Long getIdEtudiant() { return idEtudiant; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public BigDecimal getNote() { return note; }
}
