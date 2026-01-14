package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Devoir;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.services.DevoirService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/devoirs")
public class DevoirController {


    private final DevoirService devoirService;

    public DevoirController(DevoirService devoirService) {
        this.devoirService = devoirService;
    }

    @GetMapping
    public ResponseEntity<List<Devoir>> getAllDevoirs() {
        return new ResponseEntity<>(devoirService.findAll(), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Devoir> createDevoir(@RequestBody Devoir devoir) {
        Devoir devoirCreated = devoirService.save(devoir);
        return new ResponseEntity<>(devoirCreated, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devoir> getDevoir(@PathVariable Long id) {
        Optional<Devoir> devoir = devoirService.findById(id);

        return devoir
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Devoir> updateDevoir(
            @PathVariable Long id,
            @RequestBody Devoir devoirDetails) {

        Optional<Devoir> devoir = devoirService.findById(id);

        if (devoir.isPresent()) {
            Devoir existingDevoir = devoir.get();

            existingDevoir.setDescription(devoirDetails.getDescription());
            existingDevoir.setCategorie(devoirDetails.getCategorie());
            existingDevoir.setCoefficient(devoirDetails.getCoefficient());

            Devoir devoirUpdated = devoirService.save(existingDevoir);
            return new ResponseEntity<>(devoirUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}






















