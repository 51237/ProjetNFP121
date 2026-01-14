package paradigmesdeprogrammation.projetnfp121.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;

import java.util.List;

@RestController
@RequestMapping("/etudiants")
public class EtudiantService {

    @Autowired private EtudiantRepository etudiantRepository;

    @GetMapping("/")
    List<Etudiant> getAllEtudiants()    {
      return etudiantRepository.findAll();
    }

    @GetMapping("/{id}")
    Etudiant getEtudiantById(@PathVariable Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    @GetMapping("/classe/{classe_id}")
    List<Etudiant> getEtudiantsByClasse(@PathVariable Long classe_id) {
        return etudiantRepository.getEtudiantsByClasse_Id(classe_id);
    }

    @GetMapping("/disponible")
    List<Etudiant> getEtudiantsDisponible() {
        return getEtudiantsByClasse(null);
    }

    @PostMapping("/add")
    void addEtudiant(@RequestBody Etudiant etudiant) {
        etudiantRepository.save(etudiant);
    }

}
