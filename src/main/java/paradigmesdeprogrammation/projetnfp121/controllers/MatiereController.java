package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.services.MatiereService;

import java.util.List;
import java.util.Optional;

@Controller
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @GetMapping("/matieres")
    public String afficherMatieres(Model model) {
        model.addAttribute("matieres", matiereService.findAll());
        return "matiere";
    }

    @GetMapping("/matieres/new")
    public String formCreateMatiere(Model model) {
        model.addAttribute("matiere", new Matiere());
        return "matiere_new";
    }

    @PostMapping("/matieres")
    public String createMatiere(@RequestParam("denomination") String denomination, Model model) {
        Matiere m = new Matiere();
        m.setDenomination(denomination);
        matiereService.save(m);
        return "redirect:/matieres";
    }

    @GetMapping("/matieres/{id}/edit")
    public String formEditMatiere(@PathVariable Long id, Model model) {
        Matiere matiere = matiereService.findById(id).orElseThrow();
        model.addAttribute("matiere", matiere);
        return "matiere_edit";
    }

    @PostMapping("/matieres/{id}/edit")
    public String updateMatiere(@PathVariable Long id,
                                @RequestParam("denomination") String denomination) {

        Matiere matiere = matiereService.findById(id).orElseThrow();
        matiere.setDenomination(denomination);
        matiereService.save(matiere);

        return "redirect:/matieres";
    }

    @PostMapping("/matieres/{id}/delete")
    public String deleteMatiere(@PathVariable Long id, Model model) {
        try {
            matiereService.delete(id);
            return "redirect:/matieres";
        } catch (RuntimeException e) {
            // On ré-affiche la liste avec un message d’erreur (simple)
            model.addAttribute("matieres", matiereService.findAll());
            model.addAttribute("error", e.getMessage());
            return "matiere";
        }
    }

    @ResponseBody
    @GetMapping("/api/matieres")
    public ResponseEntity<List<Matiere>> apiGetAllMatieres() {
        return new ResponseEntity<>(matiereService.findAll(), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/api/matieres")
    public ResponseEntity<Matiere> apiCreateMatiere(@RequestBody Matiere matiere) {
        Matiere matiereCreated = matiereService.save(matiere);
        return new ResponseEntity<>(matiereCreated, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/api/matieres/{id}")
    public ResponseEntity<Matiere> apiGetMatiere(@PathVariable Long id) {
        Optional<Matiere> matiere = matiereService.findById(id);
        return matiere
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ResponseBody
    @PutMapping("/api/matieres/{id}")
    public ResponseEntity<Matiere> apiUpdateMatiere(@PathVariable Long id, @RequestBody Matiere matiereDetails) {
        Optional<Matiere> matiere = matiereService.findById(id);

        if (matiere.isPresent()) {
            Matiere existingMatiere = matiere.get();
            existingMatiere.setDenomination(matiereDetails.getDenomination());

            Matiere matiereUpdated = matiereService.save(existingMatiere);
            return new ResponseEntity<>(matiereUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @DeleteMapping("/api/matieres/{id}")
    public ResponseEntity<?> apiDeleteMatiere(@PathVariable Long id) {
        if (matiereService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            matiereService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
