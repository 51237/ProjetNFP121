package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.services.*;

@Controller
public class IndexController {

    private final MatiereService matiereService;
    private final EtudiantService etudiantService;
    private final ClasseService classeService;
    private final DevoirService devoirService;

    public IndexController(MatiereService matiereService, EtudiantService etudiantService, ClasseService classeService, DevoirService devoirService) {
        this.matiereService = matiereService;
        this.etudiantService = etudiantService;
        this.classeService = classeService;
        this.devoirService = devoirService;
    }

    @GetMapping({"/index", "/"})
    public String home() {
        return "index";
    }

    @GetMapping("/etudiants")
    public String AfficherEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        return "etudiant";
    }

    @GetMapping("/devoirs")
    public String AfficherDevoirs(Model model) {
        model.addAttribute("devoirs", devoirService.findAll());
        return "devoir";
    }
}
