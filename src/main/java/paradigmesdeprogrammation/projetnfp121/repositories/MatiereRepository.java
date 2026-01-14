package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;

import java.util.List;
import java.util.Optional;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {

    @Override
    List<Matiere> findAll();

    @Override
    Optional<Matiere> findById(Long id);
}
