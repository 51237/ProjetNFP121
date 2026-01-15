package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.stereotype.Controller; // IMPORTANT : On passe en Controller classique
import org.springframework.ui.Model; // Nécessaire pour la vue
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;
import paradigmesdeprogrammation.projetnfp121.services.EtudiantService;

import java.util.List;
import java.util.Optional;

@Controller // 1. On change @RestController en @Controller pour gérer le HTML
public class EtudiantController {

    private EtudiantRepository etudiantRepository;
    private EtudiantService etudiantService;

    public EtudiantController(EtudiantRepository etudiantRepository, EtudiantService etudiantService) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantService = etudiantService;
    }

    // =========================================================================
    // PARTIE VUE HTML (Ce qui corrige ton erreur 404)
    // =========================================================================

    @GetMapping("/etudiants/{id}") // L'URL appelée par ton bouton "Voir"
    public String voirEtudiant(@PathVariable Long id, Model model) {
        // On utilise ton repository existant pour trouver l'étudiant
        Etudiant e = etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Etudiant introuvable"));

        // On envoie l'étudiant à la page HTML
        model.addAttribute("etudiant", e);

        // Comme je n'ai pas tes notes dans le code fourni, on envoie null pour éviter que la page plante
        // La page affichera "N/A" ou "Aucune note" proprement grâce au code HTML que je t'ai donné avant.
        model.addAttribute("notes", null);
        model.addAttribute("moyenneGenerale", null);
        model.addAttribute("classe", e.getIdclasse());

        return "info_etudiant"; // Redirige vers info_etudiant.html
    }

    // =========================================================================
    // PARTIE API (JSON) - Ton code original adapté
    // =========================================================================
    // J'ajoute @ResponseBody partout et je précise "/api/etudiants" dans les chemins

    @GetMapping("/api/etudiants/")
    @ResponseBody
    List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @GetMapping("/api/etudiants/classe/{classe_id}")
    @ResponseBody
    List<Etudiant> getEtudiantsByClasse(@PathVariable Long classe_id) {
        return etudiantRepository.findByIdclasse_Id(classe_id);
    }

    @GetMapping("/api/etudiants/disponible")
    @ResponseBody
    List<Etudiant> getEtudiantsDisponible() {
        return etudiantRepository.findByIdclasseIsNull();
    }

    // J'ai gardé le nom de ta méthode "getMatiere" même si elle renvoie un Etudiant, pour ne rien changer.
    @GetMapping("/api/etudiants/{id}")
    @ResponseBody
    public ResponseEntity<Etudiant> getMatiere(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.findById(id);
        return etudiant.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/api/etudiants")
    @ResponseBody
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant e) {
        Etudiant created = etudiantService.save(e);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/api/etudiants/{id}")
    @ResponseBody
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id,
                                                   @RequestBody Etudiant etudiantDetails) {
        Etudiant updated = etudiantService.updateEtudiant(id, etudiantDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/api/etudiants/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        if (etudiantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        etudiantService.deleteEtudiantAvecNotes(id);
        return ResponseEntity.noContent().build();
    }
}