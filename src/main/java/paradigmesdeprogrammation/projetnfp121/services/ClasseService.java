package paradigmesdeprogrammation.projetnfp121.services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.repositories.ClasseRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.MatiereRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClasseService {
    private final ClasseRepository classeRepository;
    private final EtudiantRepository etudiantRepository;

    public ClasseService(ClasseRepository classeRepository, EtudiantRepository etudiantRepository ) {
        this.classeRepository = classeRepository;
        this.etudiantRepository = etudiantRepository;
    }

    public List<Classe> findAll() { return classeRepository.findAll();}

    public Optional<Classe> findById(Long id) {
        return classeRepository.findById(id);
    }

    public Classe save(Classe c) {return classeRepository.save(c);
    }
 public boolean delete(Long id) {
        if (etudiantRepository.existsById(id)) return false;
        classeRepository.deleteById(id);
        return true;
    }
}
