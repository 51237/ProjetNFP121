package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;
import paradigmesdeprogrammation.projetnfp121.services.EtudiantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {

    private EtudiantRepository etudiantRepository;
    private EtudiantService etudiantService;

    public EtudiantController(EtudiantRepository etudiantRepository, EtudiantService etudiantService) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantService = etudiantService;
    }
    @GetMapping("/")
    List<Etudiant> getAllEtudiants()    {
        return etudiantRepository.findAll();
    }

//    @GetMapping("/{id}")
//    Etudiant getEtudiantById(@PathVariable Long id) {
//        return etudiantRepository.findById(id).orElse(null);
//    }

    @GetMapping("/classe/{classe_id}")
    List<Etudiant> getEtudiantsByClasse(@PathVariable Long classe_id) {
        return etudiantRepository.getEtudiantsByClasse_Id(classe_id);
    }

    @GetMapping("/disponible")
    List<Etudiant> getEtudiantsDisponible() {
        return etudiantRepository.findByClasseIsNull();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getMatiere(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.findById(id);

        return etudiant.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant e) {
        Etudiant created = etudiantService.save(e);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id,
                                                   @RequestBody Etudiant etudiantDetails) {
        Etudiant updated = etudiantService.updateEtudiant(id, etudiantDetails);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        if (etudiantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        etudiantService.deleteEtudiantAvecNotes(id);
        return ResponseEntity.noContent().build();
        }

    }
