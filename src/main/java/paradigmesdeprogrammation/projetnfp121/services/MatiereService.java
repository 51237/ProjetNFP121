package paradigmesdeprogrammation.projetnfp121.services;

import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.repositories.DevoirRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.MatiereRepository;

import java.util.List;
import java.util.Optional;

public class MatiereService {

    private final MatiereRepository matiereRepository;
    private final DevoirRepository devoirRepository;

    public MatiereService(MatiereRepository matiereRepository, DevoirRepository devoirRepository ) {
        this.matiereRepository = matiereRepository;
        this.devoirRepository = devoirRepository;
    }

    public List<Matiere> findAll() {
        return matiereRepository.findAll();
    }

    public Optional<Matiere> findById(Long id) {
        return matiereRepository.findById(id);
    }

    public Matiere save(Matiere m) {
        return matiereRepository.save(m);
    }

    public boolean delete(Long id) {
        if (devoirRepository.existsById(id)) return false;
        matiereRepository.deleteById(id);
        return true;
    }

}
