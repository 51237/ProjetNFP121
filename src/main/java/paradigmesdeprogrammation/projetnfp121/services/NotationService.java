package paradigmesdeprogrammation.projetnfp121.services;

import paradigmesdeprogrammation.projetnfp121.repositories.NotationRepository;

public class NotationService {

    private NotationRepository notationRepository;

    public NotationService(NotationRepository notationRepository) {
        this.notationRepository = notationRepository;
    }

}
