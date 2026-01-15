package paradigmesdeprogrammation.projetnfp121.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import paradigmesdeprogrammation.projetnfp121.dto.NoteView;
import paradigmesdeprogrammation.projetnfp121.entities.*;
import paradigmesdeprogrammation.projetnfp121.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class DevoirService {

    private final DevoirRepository devoirRepository;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;
    private final EtudiantRepository etudiantRepository;
    private final NotationRepository notationRepository;

    public DevoirService(DevoirRepository devoirRepository,
                         ClasseRepository classeRepository,
                         MatiereRepository matiereRepository,
                         EtudiantRepository etudiantRepository,
                         NotationRepository notationRepository) {
        this.devoirRepository = devoirRepository;
        this.classeRepository = classeRepository;
        this.matiereRepository = matiereRepository;
        this.etudiantRepository = etudiantRepository;
        this.notationRepository = notationRepository;
    }

    public List<Devoir> findAll() {
        return devoirRepository.findAll();
    }

    public Optional<Devoir> findById(Long id) {
        return devoirRepository.findById(id);
    }

    public boolean hasNotations(Long devoirId) {
        return notationRepository.existsByIddevoir_Id(devoirId);
    }

    @Transactional
    public Devoir createDevoir(Long classeId, Long matiereId, String description, String categorie, BigDecimal coefficient) {
        Classe c = classeRepository.findById(classeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classe not found"));

        Matiere m = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matiere not found"));

        Devoir d = new Devoir();
        d.setIdclasse(c);
        d.setIdmatiere(m);
        d.setDescription(description);
        d.setCategorie(categorie);

        if (coefficient == null) coefficient = new BigDecimal("1.0");
        d.setCoefficient(coefficient);

        d.setDateDeCreation(LocalDate.now());

        return devoirRepository.save(d);
    }

    @Transactional
    public Devoir updateDevoir(Long devoirId,
                               Long classeId,
                               Long matiereId,
                               String description,
                               String categorie,
                               BigDecimal coefficient) {

        Devoir d = devoirRepository.findById(devoirId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devoir not found"));

        boolean hasNotes = notationRepository.existsByIddevoir_Id(devoirId);

        d.setDescription(description);
        d.setCategorie(categorie);
        if (coefficient == null) coefficient = new BigDecimal("1.0");
        d.setCoefficient(coefficient);

        if (!hasNotes) {
            Classe c = classeRepository.findById(classeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classe not found"));

            Matiere m = matiereRepository.findById(matiereId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matiere not found"));

            d.setIdclasse(c);
            d.setIdmatiere(m);
        }

        return devoirRepository.save(d);
    }

    @Transactional
    public void deleteDevoirAvecNotations(Long devoirId) {
        notationRepository.deleteByIddevoir_Id(devoirId);
        devoirRepository.deleteById(devoirId);
    }


    public List<NoteView> getLignesNotation(Long devoirId) {

        Devoir d = devoirRepository.findById(devoirId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devoir not found"));

        Long classeId = d.getIdclasse().getId();

        List<Etudiant> etudiants = etudiantRepository.findByIdclasse_Id(classeId);
        List<Notation> notations = notationRepository.findByIddevoir_Id(devoirId);

        Map<Long, BigDecimal> noteParEtudiant = new HashMap<>();
        for (Notation n : notations) {
            if (n.getNote() != null) {
                noteParEtudiant.put(n.getIdetudiant().getId(), n.getNote());
            } else {
                noteParEtudiant.put(n.getIdetudiant().getId(), null);
            }
        }

        List<NoteView> lignes = new ArrayList<>();
        for (Etudiant e : etudiants) {
            lignes.add(new NoteView(
                    e.getId(),
                    e.getNom(),
                    e.getPrenom(),
                    noteParEtudiant.get(e.getId())
            ));
        }
        return lignes;
    }

    @Transactional
    public void saveNotations(Long devoirId, Map<Long, BigDecimal> notesParEtudiant) {

        Devoir d = devoirRepository.findById(devoirId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Devoir not found"));

        Long classeId = d.getIdclasse().getId();
        List<Etudiant> etudiants = etudiantRepository.findByIdclasse_Id(classeId);

        Set<Long> idsValides = new HashSet<>();
        for (Etudiant e : etudiants) idsValides.add(e.getId());

        for (Map.Entry<Long, BigDecimal> entry : notesParEtudiant.entrySet()) {
            Long etudiantId = entry.getKey();
            BigDecimal note = entry.getValue();

            if (!idsValides.contains(etudiantId)) continue;

            if (note != null) {
                if (note.compareTo(BigDecimal.ZERO) < 0 || note.compareTo(new BigDecimal("20")) > 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note must be between 0 and 20");
                }
            }

            Optional<Notation> opt =
                    notationRepository.findById_IddevoirAndId_Idetudiant(devoirId, etudiantId);

            Etudiant e = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Etudiant not found"));

            if (opt.isPresent()) {
                Notation n = opt.get();
                n.setNote(note);
                notationRepository.save(n);
            } else {
                Notation n = new Notation();

                n.setId(new NotationId());

                n.setIddevoir(d);
                n.setIdetudiant(e);
                n.setNote(note);

                notationRepository.save(n);
            }
        }
    }
}
