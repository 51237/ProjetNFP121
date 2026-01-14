package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    @Modifying
    @Query("UPDATE Etudiant e SET e.classe = NULL WHERE e.classe.id = :classeId")
    int detachementEtudiantsFromClasse(@Param("classeId") Long classeId);

    List<Etudiant> getEtudiantsByClasse_Id(Long id);

    @Override
    List<Etudiant> findAll();

    @Override
    Optional<Etudiant> findById(Long id);

    List<Etudiant> findByClasseIsNull();

}
