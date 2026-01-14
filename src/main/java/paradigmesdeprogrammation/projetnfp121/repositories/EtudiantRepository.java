package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    List<Etudiant> getEtudiantsByClasse_Id(Long id);

    @Override
    List<Etudiant> findAll();

    @Override
    Optional<Etudiant> findById(Long id);

    List<Etudiant> findByClasseIsNull();

}
