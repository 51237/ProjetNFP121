package paradigmesdeprogrammation.projetnfp121.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.repositories.ClasseRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.MatiereRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.NotationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClasseService {
    private final ClasseRepository classeRepository;
    private final EtudiantRepository etudiantRepository;
    private final NotationRepository notationRepository;

    public ClasseService(ClasseRepository classeRepository, EtudiantRepository etudiantRepository, NotationRepository notationRepository ) {
        this.classeRepository = classeRepository;
        this.etudiantRepository = etudiantRepository;
        this.notationRepository = notationRepository;
    }

    public List<Classe> findAll() { return classeRepository.findAll();}

    public Optional<Classe> findById(Long id) {
        return classeRepository.findById(id);
    }

    public Classe save(Classe c) {return classeRepository.save(c);
    }

    @Transactional
    public void deleteEtudiantAvecNotes(Long id) {
        notationRepository.deleteByIdetudiant_Id(id);
        classeRepository.deleteById(id);
    }


}
