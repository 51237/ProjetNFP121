package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import paradigmesdeprogrammation.projetnfp121.entities.Devoir;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;

import java.util.List;
import java.util.Optional;

public interface DevoirRepository extends JpaRepository<Devoir, Long> {

    List<Devoir> getDevoirByClasse_Id(Long id);

    List<Devoir> getDevoirByMatiere_Id(Long id);

    @Override
    List<Devoir> findAll();

    @Override
    Optional<Devoir> findById(Long id);

}
