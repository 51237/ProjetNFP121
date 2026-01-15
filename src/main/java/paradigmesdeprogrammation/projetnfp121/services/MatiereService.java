package paradigmesdeprogrammation.projetnfp121.services;

import org.springframework.stereotype.Service;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.repositories.DevoirRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.MatiereRepository;

import java.util.List;
import java.util.Optional;

@Service
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

    public Matiere create(Matiere matiere) {
        return matiereRepository.save(matiere);
    }

    public Matiere update(Long id, Matiere matiereDetails) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière inexistante"));

        matiere.setDenomination(matiereDetails.getDenomination());
        return matiereRepository.save(matiere);
    }

    public void delete(Long id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière inexistante"));

        long nbDevoirs = devoirRepository.countByIdmatiere(matiere);

        if (nbDevoirs > 0) {
            throw new RuntimeException("Impossible de supprimer : matière utilisée dans un devoir");
        }

        matiereRepository.delete(matiere);
    }
}
