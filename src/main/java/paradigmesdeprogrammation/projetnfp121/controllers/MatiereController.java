package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.services.MatiereService;

import java.util.List;


@RestController
@RequestMapping("/matieres")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {

        this.matiereService = matiereService;
    }

    @GetMapping
    public ResponseEntity<List<Matiere>> getAllMatiere() {
        return new ResponseEntity<>(matiereService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Matiere> createMatiere(@RequestBody Matiere matiere) {
        Matiere MatiereCreated = matiereService.save(matiere);
        return new ResponseEntity<>(MatiereCreated, HttpStatus.CREATED);
    }

}
