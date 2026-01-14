package paradigmesdeprogrammation.projetnfp121.services;

import org.springframework.stereotype.Service;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.entities.Devoir;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.repositories.ClasseRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.DevoirRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;

import java.util.List;
import java.util.Optional;


@Service
public class DevoirService {
    private final DevoirRepository devoirRepository;


    public DevoirService(DevoirRepository devoirRepository) {
        this.devoirRepository = devoirRepository;

    }

    public List<Devoir> findAll() { return devoirRepository.findAll();}

    public Optional<Devoir> findById(Long id) {
        return devoirRepository.findById(id);
    }

    public Devoir save(Devoir d) {
        return devoirRepository.save(d);

    }

    public void delete(Long id) {
        devoirRepository.deleteById(id);
    }

}
