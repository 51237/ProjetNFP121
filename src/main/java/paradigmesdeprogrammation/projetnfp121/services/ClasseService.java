package paradigmesdeprogrammation.projetnfp121.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import paradigmesdeprogrammation.projetnfp121.dto.BulletinView;
import paradigmesdeprogrammation.projetnfp121.entities.Classe;
import paradigmesdeprogrammation.projetnfp121.entities.Etudiant;
import paradigmesdeprogrammation.projetnfp121.entities.Notation;
import paradigmesdeprogrammation.projetnfp121.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ClasseService {
    private final ClasseRepository classeRepository;
    private final EtudiantRepository etudiantRepository;
    private final NotationRepository notationRepository;
    private final DevoirRepository devoirRepository;

    public ClasseService(ClasseRepository classeRepository, EtudiantRepository etudiantRepository, NotationRepository notationRepository, DevoirRepository devoirRepository) {
        this.classeRepository = classeRepository;
        this.etudiantRepository = etudiantRepository;
        this.notationRepository = notationRepository;
        this.devoirRepository = devoirRepository;
    }

    public List<Classe> findAll() {
        return classeRepository.findAll();
    }

    public Optional<Classe> findById(Long id) {
        return classeRepository.findById(id);
    }

    public Classe save(Classe c) {
        return classeRepository.save(c);
    }

    @Transactional
    public BulletinView calculerBulletinEtudiant(Long classeId, Long etudiantId) {

        Etudiant e = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Etudiant not found"));

        if (e.getIdclasse() == null || !e.getIdclasse().getId().equals(classeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Etudiant not in this classe");
        }

        List<Notation> notes = notationRepository
                .findByIdetudiant_IdAndIddevoir_Idclasse_Id(etudiantId, classeId);

        Map<String, BigDecimal> sommePonderee = new LinkedHashMap<>();
        Map<String, BigDecimal> sommeCoef = new LinkedHashMap<>();

        BigDecimal sommeGen = BigDecimal.ZERO;
        BigDecimal coefGen = BigDecimal.ZERO;

        for (Notation n : notes) {
            if (n.getNote() == null) continue;

            String matiere = n.getIddevoir().getIdmatiere().getDenomination();
            BigDecimal note = n.getNote();
            BigDecimal coef = n.getIddevoir().getCoefficient();
            if (coef == null) continue;

            sommePonderee.merge(matiere, note.multiply(coef), BigDecimal::add);
            sommeCoef.merge(matiere, coef, BigDecimal::add);

            sommeGen = sommeGen.add(note.multiply(coef));
            coefGen = coefGen.add(coef);
        }

        Map<String, BigDecimal> moyennesParMatiere = new LinkedHashMap<>();
        for (String matiere : sommePonderee.keySet()) {
            BigDecimal avg = sommePonderee.get(matiere)
                    .divide(sommeCoef.get(matiere), 2, RoundingMode.HALF_UP);
            moyennesParMatiere.put(matiere, avg);
        }

        BigDecimal moyenneGenerale = coefGen.compareTo(BigDecimal.ZERO) == 0
                ? null
                : sommeGen.divide(coefGen, 2, RoundingMode.HALF_UP);

        return new BulletinView(
                e.getId(),
                e.getNom(),
                e.getPrenom(),
                moyennesParMatiere,
                moyenneGenerale
        );
    }

    @Transactional
    public BulletinView calculerReleveEtudiant(Long etudiantId) {

        Etudiant e = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Etudiant not found"));

        if (e.getIdclasse() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Etudiant has no classe");
        }

        return calculerBulletinEtudiant(e.getIdclasse().getId(), etudiantId);
    }


    @Transactional
    public void deleteClasseAndClearEtudiant(Long classeId) {

        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classe not found"));

        etudiantRepository.detachementEtudiantsFromClasse(classeId);

        notationRepository.deleteByClasseId(classeId);

        devoirRepository.deleteByIdclasse_Id(classeId);

        classeRepository.delete(classe);

    }

    @Transactional
    public Classe createClasse(String denomination, List<Long> etudiantsIds) {
        Classe c = new Classe();
        c.setDenomination(denomination);
        Classe saved = classeRepository.save(c);

        if (etudiantsIds != null && !etudiantsIds.isEmpty()) {
            List<Etudiant> etudiants = etudiantRepository.findAllById(etudiantsIds);
            for (Etudiant e : etudiants) {
                if (e.getIdclasse() == null) {
                    e.setIdclasse(saved);
                }
            }
            etudiantRepository.saveAll(etudiants);
        }
        return saved;
    }

    @Transactional
    public void updateClasse(Long classeId, String denomination,
                             List<Long> addIds, List<Long> removeIds) {

        Classe c = classeRepository.findById(classeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        c.setDenomination(denomination);

        if (removeIds != null && !removeIds.isEmpty()) {
            List<Etudiant> toRemove = etudiantRepository.findAllById(removeIds);
            for (Etudiant e : toRemove) {
                if (e.getIdclasse() != null && e.getIdclasse().getId().equals(classeId)) {
                    e.setIdclasse(null);
                }
            }
            etudiantRepository.saveAll(toRemove);
        }

        if (addIds != null && !addIds.isEmpty()) {
            List<Etudiant> toAdd = etudiantRepository.findAllById(addIds);
            for (Etudiant e : toAdd) {
                if (e.getIdclasse() == null) {
                    e.setIdclasse(c);
                }
            }
            etudiantRepository.saveAll(toAdd);
        }

        classeRepository.save(c);
    }

}
