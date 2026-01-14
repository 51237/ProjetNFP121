package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.entities.Notation;

import java.util.List;
import java.util.Optional;

public interface NotationRepository extends JpaRepository<Notation,Long> {

    @Override
    List<Notation> findAll();

    @Override
    Optional<Notation> findById(Long id);

    boolean existsByIdetudiant_Id(Long idEtudiant);

    boolean existsByIddevoir_Id(Long idDevoir);

    void deleteByIdetudiant_Id(Long idEtudiant);

}
