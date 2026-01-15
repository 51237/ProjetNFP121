package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.dto.NoteView;
import paradigmesdeprogrammation.projetnfp121.entities.Devoir;
import paradigmesdeprogrammation.projetnfp121.repositories.ClasseRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.MatiereRepository;
import paradigmesdeprogrammation.projetnfp121.services.DevoirService;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class DevoirController {

    private final DevoirService devoirService;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;

    public DevoirController(DevoirService devoirService,
                            ClasseRepository classeRepository,
                            MatiereRepository matiereRepository) {
        this.devoirService = devoirService;
        this.classeRepository = classeRepository;
        this.matiereRepository = matiereRepository;
    }

    // =========================
    // VUES HTML (Thymeleaf)
    // =========================

    @GetMapping("/devoirs")
    public String afficherDevoirs(Model model) {
        model.addAttribute("devoirs", devoirService.findAll());
        return "devoir";
    }

    @GetMapping("/devoirs/new")
    public String formCreateDevoir(Model model) {
        model.addAttribute("classes", classeRepository.findAll());
        model.addAttribute("matieres", matiereRepository.findAll());
        return "devoir_new";
    }

    @PostMapping("/devoirs")
    public String createDevoir(@RequestParam("classeId") Long classeId,
                               @RequestParam("matiereId") Long matiereId,
                               @RequestParam("description") String description,
                               @RequestParam("categorie") String categorie,
                               @RequestParam(value = "coefficient", required = false) BigDecimal coefficient) {

        devoirService.createDevoir(classeId, matiereId, description, categorie, coefficient);
        return "redirect:/devoirs";
    }

    @GetMapping("/devoirs/{id}/edit")
    public String formEditDevoir(@PathVariable Long id, Model model) {
        Devoir d = devoirService.findById(id).orElseThrow();
        boolean hasNotes = devoirService.hasNotations(id);

        model.addAttribute("devoir", d);
        model.addAttribute("hasNotes", hasNotes);
        model.addAttribute("classes", classeRepository.findAll());
        model.addAttribute("matieres", matiereRepository.findAll());

        return "devoir_edit";
    }

    @PostMapping("/devoirs/{id}/edit")
    public String updateDevoir(@PathVariable Long id,
                               @RequestParam("classeId") Long classeId,
                               @RequestParam("matiereId") Long matiereId,
                               @RequestParam("description") String description,
                               @RequestParam("categorie") String categorie,
                               @RequestParam(value = "coefficient", required = false) BigDecimal coefficient) {

        devoirService.updateDevoir(id, classeId, matiereId, description, categorie, coefficient);
        return "redirect:/devoirs";
    }

    @PostMapping("/devoirs/{id}/delete")
    public String deleteDevoir(@PathVariable Long id) {
        devoirService.deleteDevoirAvecNotations(id);
        return "redirect:/devoirs";
    }

    // ---- Notation HTML ----

    @GetMapping("/devoirs/{id}/notations")
    public String formNotations(@PathVariable Long id, Model model) {
        Devoir d = devoirService.findById(id).orElseThrow();
        List<NoteView> lignes = devoirService.getLignesNotation(id);

        model.addAttribute("devoir", d);
        model.addAttribute("lignes", lignes);

        return "devoir_notation";
    }

    @PostMapping("/devoirs/{id}/notations")
    public String saveNotations(@PathVariable Long id,
                                @RequestParam Map<String, String> params) {

        // On lit des champs "note_12" -> "15.5"
        Map<Long, BigDecimal> notes = new HashMap<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith("note_")) continue;

            Long etudiantId = Long.parseLong(key.substring("note_".length()));
            String value = entry.getValue();

            if (value == null || value.isBlank()) {
                notes.put(etudiantId, null);
            } else {
                notes.put(etudiantId, new BigDecimal(value));
            }
        }

        devoirService.saveNotations(id, notes);
        return "redirect:/devoirs";
    }

    // =========================
    // API (JSON)
    // =========================

    @ResponseBody
    @GetMapping("/api/devoirs")
    public ResponseEntity<List<Devoir>> apiGetAll() {
        return new ResponseEntity<>(devoirService.findAll(), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/api/devoirs/{id}")
    public ResponseEntity<Devoir> apiGet(@PathVariable Long id) {
        return devoirService.findById(id)
                .map(d -> new ResponseEntity<>(d, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ResponseBody
    @PostMapping("/api/devoirs")
    public ResponseEntity<Devoir> apiCreate(@RequestBody Devoir devoir) {
        // Pour l’API, on force date + coefficient si null
        if (devoir.getCoefficient() == null) devoir.setCoefficient(new BigDecimal("1.0"));
        if (devoir.getDateDeCreation() == null) devoir.setDateDeCreation(java.time.LocalDate.now());
        Devoir saved = devoirService.findById(devoir.getId()).orElse(null) == null
                ? devoirService.findById(devoir.getId()).orElse(null)
                : null;
        // plus simple : on utilise le repo via service save si tu veux, mais attention aux règles.
        // Ici on conseille plutôt de passer par createDevoir côté HTML.
        Devoir created = devoirService.createDevoir(
                devoir.getIdclasse().getId(),
                devoir.getIdmatiere().getId(),
                devoir.getDescription(),
                devoir.getCategorie(),
                devoir.getCoefficient()
        );
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/api/devoirs/{id}")
    public ResponseEntity<Devoir> apiUpdate(@PathVariable Long id, @RequestBody Devoir devoirDetails) {
        Devoir updated = devoirService.updateDevoir(
                id,
                devoirDetails.getIdclasse().getId(),
                devoirDetails.getIdmatiere().getId(),
                devoirDetails.getDescription(),
                devoirDetails.getCategorie(),
                devoirDetails.getCoefficient()
        );
        return ResponseEntity.ok(updated);
    }

    @ResponseBody
    @DeleteMapping("/api/devoirs/{id}")
    public ResponseEntity<Void> apiDelete(@PathVariable Long id) {
        if (devoirService.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        devoirService.deleteDevoirAvecNotations(id);
        return ResponseEntity.noContent().build();
    }
}
