package paradigmesdeprogrammation.projetnfp121.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;

import java.util.List;
import java.util.Optional;

public interface ClasseRepository extends JpaRepository<Classe, Long> {

    @Override
    List<Classe> findAll();

    @Override
    Optional<Classe> findById(Long id);

}
