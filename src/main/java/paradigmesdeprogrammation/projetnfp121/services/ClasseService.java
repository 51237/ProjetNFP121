package paradigmesdeprogrammation.projetnfp121.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.repositories.ClasseRepository;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClasseService {

    @Autowired private ClasseRepository classeRepository;

    @GetMapping("/")
    List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    @GetMapping("/{id}")
    Classe getClasseById(@PathVariable Long id) {
        return classeRepository.findById(id).orElse(null);
    }

    @PostMapping("/add")
    void addClasse(@RequestBody Classe classe) {
        classeRepository.save(classe);
    }

}
