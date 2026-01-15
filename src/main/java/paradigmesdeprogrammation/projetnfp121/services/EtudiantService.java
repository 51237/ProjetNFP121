package paradigmesdeprogrammation.projetnfp121.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paradigmesdeprogrammation.projetnfp121.dto.BulletinView;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.repositories.EtudiantRepository;
import paradigmesdeprogrammation.projetnfp121.repositories.NotationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final NotationRepository notationRepository;

    public EtudiantService(EtudiantRepository etudiantRepository, NotationRepository notationRepository) {
        this.etudiantRepository = etudiantRepository;
        this.notationRepository = notationRepository;
    }

    public Etudiant updateEtudiant(Long id, Etudiant etudiantDetails) {
        Etudiant existingEtudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Etudiant not found"));

        boolean hasNotes = notationRepository.existsById_Idetudiant(id);

        existingEtudiant.setNom(etudiantDetails.getNom());
        existingEtudiant.setPrenom(etudiantDetails.getPrenom());

        if (!hasNotes) {
            existingEtudiant.setIdclasse(etudiantDetails.getIdclasse());
        }

        return etudiantRepository.save(existingEtudiant);
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

    @Transactional
    public void deleteEtudiantAvecNotes(Long id) {
        notationRepository.deleteByIdetudiant_Id(id);
        etudiantRepository.deleteById(id);
    }

}
