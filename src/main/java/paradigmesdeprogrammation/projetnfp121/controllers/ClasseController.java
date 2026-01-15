package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import paradigmesdeprogrammation.projetnfp121.dto.BulletinView;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;
import paradigmesdeprogrammation.projetnfp121.services.ClasseService;

import java.util.List;
import java.util.Optional;

@Controller
public class ClasseController {

    private final ClasseService classeService;
    private final EtudiantRepository etudiantRepository;

    public ClasseController(ClasseService classeService, EtudiantRepository etudiantRepository) {
        this.classeService = classeService;
        this.etudiantRepository = etudiantRepository;
    }

    @GetMapping("/classes")
    public String afficherClasses(Model model) {
        model.addAttribute("classes", classeService.findAll());
        return "classe"; // templates/classe.html
    }

    @GetMapping("/classes/{id}/etudiants")
    public String etudiantsDeClasse(@PathVariable Long id, Model model) {
        Classe classe = classeService.findById(id).orElseThrow();
        model.addAttribute("classe", classe);
        model.addAttribute("etudiants", etudiantRepository.findByIdclasse_Id(id));
        return "classe_etudiants";
    }


    @GetMapping("/classes/{classeId}/bulletin/{etudiantId}")
    public String bulletinEtudiant(@PathVariable Long classeId,
                                   @PathVariable Long etudiantId,
                                   Model model) {

        model.addAttribute("classeId", classeId);
        model.addAttribute("bulletin", classeService.calculerBulletinEtudiant(classeId, etudiantId));

        return "bulletin";
    }

    @GetMapping("/classes/new")
    public String formCreateClasse(Model model) {
        model.addAttribute("classe", new Classe());
        model.addAttribute("disponibles", etudiantRepository.findByIdclasseIsNull());
        return "classe_new";
    }

    @PostMapping("/classes")
    public String createClasse(@RequestParam("denomination") String denomination,
                               @RequestParam(value = "etudiantsIds", required = false) List<Long> etudiantsIds) {

        classeService.createClasse(denomination, etudiantsIds);
        return "redirect:/classes";
    }

    @GetMapping("/classes/{id}/edit")
    public String formEditClasse(@PathVariable Long id, Model model) {
        Classe classe = classeService.findById(id).orElseThrow();

        model.addAttribute("classe", classe);
        model.addAttribute("etudiantsClasse", etudiantRepository.findByIdclasse_Id(id));
        model.addAttribute("disponibles", etudiantRepository.findByIdclasseIsNull());

        return "classe_edit";
    }

    @PostMapping("/classes/{id}/edit")
    public String updateClasse(@PathVariable Long id,
                               @RequestParam("denomination") String denomination,
                               @RequestParam(value = "addIds", required = false) List<Long> addIds,
                               @RequestParam(value = "removeIds", required = false) List<Long> removeIds) {

        classeService.updateClasse(id, denomination, addIds, removeIds);
        return "redirect:/classes";
    }

    @PostMapping("/classes/{id}/delete")
    public String deleteClasse(@PathVariable Long id) {
        classeService.deleteClasseAndClearEtudiant(id);
        return "redirect:/classes";
    }

    @ResponseBody
    @GetMapping("/api/classes")
    public ResponseEntity<List<Classe>> getAllClasses() {
        return new ResponseEntity<>(classeService.findAll(), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/api/classes")
    public ResponseEntity<Classe> apiCreateClasse(@RequestBody Classe classe) {
        Classe classeCreated = classeService.save(classe);
        return new ResponseEntity<>(classeCreated, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/api/classes/{id}")
    public ResponseEntity<Classe> apiGetClasse(@PathVariable Long id) {
        Optional<Classe> classe = classeService.findById(id);
        return classe
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ResponseBody
    @PutMapping("/api/classes/{id}")
    public ResponseEntity<Classe> apiUpdateClasse(@PathVariable Long id, @RequestBody Classe classeDetails) {
        Optional<Classe> classe = classeService.findById(id);

        if (classe.isPresent()) {
            Classe existingClasse = classe.get();
            existingClasse.setDenomination(classeDetails.getDenomination());

            Classe classeUpdated = classeService.save(existingClasse);
            return new ResponseEntity<>(classeUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @DeleteMapping("/api/classes/{id}")
    public ResponseEntity<Void> apiDeleteClasse(@PathVariable Long id) {
        if (classeService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        classeService.deleteClasseAndClearEtudiant(id);
        return ResponseEntity.noContent().build();
    }


}
