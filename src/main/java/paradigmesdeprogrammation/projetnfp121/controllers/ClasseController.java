package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.repositories.ClasseRepository;
import paradigmesdeprogrammation.projetnfp121.services.ClasseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classes")
public class ClasseController {

    @Autowired private ClasseRepository classeRepository;
    @Autowired private ClasseService classeService;


//
//    @GetMapping("/{id}")
//    Classe getClasseById(@PathVariable Long id) {
//        return classeRepository.findById(id).orElse(null);
//    }

    @GetMapping
    public ResponseEntity<List<Classe>> getAllClasses() {
        return new ResponseEntity<>(classeService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Classe> createClasse(@RequestBody Classe classe) {
        Classe classeCreated = classeService.save(classe);
        return new ResponseEntity<>(classeCreated, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classe> getClasse(@PathVariable Long id) {
        Optional<Classe> classe = classeService.findById(id);

        return classe.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classe> updateClasse(@PathVariable Long id, @RequestBody Classe classeDetails) {
        Optional<Classe> classe = classeService.findById(id);

        if (classe.isPresent()) {
            Classe existingClasse = classe.get();
            existingClasse.setDenomination(classeDetails.getDenomination());

            Classe classeUpdated = classeService.save(existingClasse);
            return new ResponseEntity<>(classeUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        if (classeService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        classeService.deleteEtudiantAvecNotes(id);
        return ResponseEntity.noContent().build();
    }
}