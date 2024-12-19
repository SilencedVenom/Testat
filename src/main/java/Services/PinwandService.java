package Services;

import Repository.PinwandRepository;
import models.PinwandBeitrag;

import java.util.List;

public class PinwandService {

    private final PinwandRepository pinwandRepository;

    public PinwandService() {
        this.pinwandRepository = new PinwandRepository();
    }
    /**
     * fügt einen Beitrag hinzu
     * @param email
     * @param beitrag
     * @param verfasser
     */
    public void addBeitragToPinwand(String email, String beitrag, String verfasser) {
        pinwandRepository.addBeitrag(email, beitrag, verfasser);
    }
    /**
     * Holt alle Beiträge eines Nutzers
     * @param email
     * @return List<PinwandBeitrag>
     *
     */
    public List<PinwandBeitrag> getBeitraegeFromPinwand(String email) {
        return pinwandRepository.getBeitraege(email);
    }
}
