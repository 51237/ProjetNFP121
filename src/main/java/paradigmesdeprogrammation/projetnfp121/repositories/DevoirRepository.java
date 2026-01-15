package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.entities.Devoir;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.entities.Matiere;

import java.util.List;
import java.util.Optional;

public interface DevoirRepository extends JpaRepository<Devoir, Long> {

    void deleteByIdclasse_Id(Long idClasse);

    @Override
    List<Devoir> findAll();

    @Override
    Optional<Devoir> findById(Long id);

    long countByIdmatiere(Matiere idmatiere);
}
