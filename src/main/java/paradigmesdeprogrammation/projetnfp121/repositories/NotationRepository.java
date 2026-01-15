package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;
import paradigmesdeprogrammation.projetnfp121.entities.Notation;

import java.util.List;
import java.util.Optional;

public interface NotationRepository extends JpaRepository<Notation, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                DELETE FROM Notation n
                WHERE n.iddevoir.id IN (
                    SELECT d.id FROM Devoir d
                    WHERE d.idclasse.id = :classeId
                )
            """)
    int deleteByClasseId(@Param("classeId") Long classeId);

    @Override
    List<Notation> findAll();

    @Override
    Optional<Notation> findById(Long id);

    boolean existsByIdetudiant_Id(Long idEtudiant);

    boolean existsByIddevoir_Id(Long idDevoir);

    void deleteByIdetudiant_Id(Long idEtudiant);

    void deleteByIddevoir_Idclasse_Id(Long classeId);

    List<Notation> findByIdetudiant_IdAndIddevoir_Idclasse_Id(Long etudiantId, Long classeId);

}
