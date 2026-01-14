package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import paradigmesdeprogrammation.projetnfp121.services.MatiereService;

public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @GetMapping("/matieres")
    public String matieres(Model model) {
        model.addAttribute("matieres",matiereService.findAll());
        return "matieres";
    }

//    @GetMapping("/matieres/ajouter")


}
