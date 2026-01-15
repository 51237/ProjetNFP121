package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.services.MatiereService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/matieres")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {

        this.matiereService = matiereService;
    }

    @GetMapping
    public ResponseEntity<List<Matiere>> getAllMatieres() {
        return new ResponseEntity<>(matiereService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Matiere> createMatiere(@RequestBody Matiere matiere) {
        Matiere matiereCreated = matiereService.save(matiere);
        return new ResponseEntity<>(matiereCreated, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matiere> getMatiere(@PathVariable Long id) {
        Optional<Matiere> matiere = matiereService.findById(id);

        return matiere.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matiere> updateMatiere(@PathVariable Long id, @RequestBody Matiere matiereDetails) {
        Optional<Matiere> matiere = matiereService.findById(id);

        if (matiere.isPresent()) {
            Matiere existingMatiere = matiere.get();
            existingMatiere.setDenomination(matiereDetails.getDenomination());

            Matiere matiereUpdated = matiereService.save(existingMatiere);
            return new ResponseEntity<>(matiereUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        Optional<Matiere> matiere = matiereService.findById(id);

        if (matiere.isPresent()) {
            matiereService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
