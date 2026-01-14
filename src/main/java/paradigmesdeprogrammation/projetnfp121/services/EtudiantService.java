package paradigmesdeprogrammation.projetnfp121.services;

import org.springframework.stereotype.Service;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> findById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Etudiant save(Etudiant e) {
        return etudiantRepository.save(e);
    }




}