package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.repositories.ClasseRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.NotationRepository;
import paradigmesdeprogrammation.projetnfp121.services.ClasseService;
import paradigmesdeprogrammation.projetnfp121.services.EtudiantService;

import java.util.List;
import java.util.Optional;

@Controller
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final ClasseRepository classeRepository;
    private final NotationRepository notationRepository;
    private final ClasseService classeService;

    public EtudiantController(EtudiantService etudiantService,
                              ClasseRepository classeRepository,
                              NotationRepository notationRepository,
                              ClasseService classeService) {

        this.etudiantService = etudiantService;
        this.classeRepository = classeRepository;
        this.notationRepository = notationRepository;
        this.classeService = classeService;
    }

    @GetMapping("/etudiants")
    public String afficherEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        return "etudiant";
    }

    @GetMapping("/etudiants/new")
    public String formCreateEtudiant(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("classes", classeRepository.findAll());
        return "etudiant_new";
    }

    @PostMapping("/etudiants")
    public String createEtudiant(@RequestParam("nom") String nom,
                                 @RequestParam("prenom") String prenom,
                                 @RequestParam(value = "photo", required = false) String photo,
                                 @RequestParam(value = "classeId", required = false) Long classeId) {

        Etudiant e = new Etudiant();
        e.setNom(nom);
        e.setPrenom(prenom);
        e.setPhoto(photo);

        if (classeId != null) {
            Classe c = classeRepository.findById(classeId).orElse(null);
            e.setIdclasse(c);
        } else {
            e.setIdclasse(null);
        }

        etudiantService.save(e);
        return "redirect:/etudiants";
    }

    @GetMapping("/etudiants/{id}/edit")
    public String formEditEtudiant(@PathVariable Long id, Model model) {
        Etudiant etudiant = etudiantService.findById(id).orElseThrow();

        boolean hasNotes = notationRepository.existsByIdetudiant_Id(id);

        model.addAttribute("etudiant", etudiant);
        model.addAttribute("hasNotes", hasNotes);

        model.addAttribute("classes", classeRepository.findAll());

        return "etudiant_edit";
    }

    @PostMapping("/etudiants/{id}/edit")
    public String updateEtudiant(@PathVariable Long id,
                                 @RequestParam("nom") String nom,
                                 @RequestParam("prenom") String prenom,
                                 @RequestParam(value = "photo", required = false) String photo,
                                 @RequestParam(value = "classeId", required = false) Long classeId,
                                 Model model) {

        Etudiant current = etudiantService.findById(id).orElseThrow();
        boolean hasNotes = notationRepository.existsByIdetudiant_Id(id);

        current.setNom(nom);
        current.setPrenom(prenom);
        current.setPhoto(photo);

        if (!hasNotes) {
            if (classeId != null) {
                Classe c = classeRepository.findById(classeId).orElse(null);
                current.setIdclasse(c);
            } else {
                current.setIdclasse(null);
            }
        }

        etudiantService.save(current);
        return "redirect:/etudiants";
    }

    @PostMapping("/etudiants/{id}/delete")
    public String deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiantAvecNotes(id);
        return "redirect:/etudiants";
    }

    @GetMapping("/etudiants/{id}/releve")
    public String releveNotes(@PathVariable Long id, Model model) {
        model.addAttribute("bulletin", classeService.calculerReleveEtudiant(id));
        return "etudiant_releve";
    }

    @ResponseBody
    @GetMapping("/api/etudiants")
    public ResponseEntity<List<Etudiant>> apiGetAllEtudiants() {
        return new ResponseEntity<>(etudiantService.findAll(), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/api/etudiants/{id}")
    public ResponseEntity<Etudiant> apiGetEtudiant(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.findById(id);
        return etudiant
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ResponseBody
    @GetMapping("/api/etudiants/classe/{classeId}")
    public ResponseEntity<List<Etudiant>> apiGetEtudiantsByClasse(@PathVariable Long classeId) {
        return ResponseEntity.ok(etudiantService.findByClasseId(classeId));
    }

    @ResponseBody
    @GetMapping("/api/etudiants/disponible")
    public ResponseEntity<List<Etudiant>> apiGetEtudiantsDisponible() {
        return ResponseEntity.ok(etudiantService.findDisponibles());
    }

    @ResponseBody
    @PostMapping("/api/etudiants")
    public ResponseEntity<Etudiant> apiCreateEtudiant(@RequestBody Etudiant e) {
        Etudiant created = etudiantService.save(e);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/api/etudiants/{id}")
    public ResponseEntity<Etudiant> apiUpdateEtudiant(@PathVariable Long id,
                                                      @RequestBody Etudiant etudiantDetails) {
        Etudiant updated = etudiantService.updateEtudiant(id, etudiantDetails);
        return ResponseEntity.ok(updated);
    }

    @ResponseBody
    @DeleteMapping("/api/etudiants/{id}")
    public ResponseEntity<Void> apiDeleteEtudiant(@PathVariable Long id) {
        if (etudiantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        etudiantService.deleteEtudiantAvecNotes(id);
        return ResponseEntity.noContent().build();
    }
}
