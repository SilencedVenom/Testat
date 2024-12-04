package Services;

import Repository.PinwandRepository;
import models.PinwandBeitrag;

import java.util.List;

public class PinwandService {

    private final PinwandRepository pinwandRepository;

    public PinwandService() {
        this.pinwandRepository = new PinwandRepository();
    }

    public void addBeitragToPinwand(String email, String beitrag, String verfasser) {
        pinwandRepository.addBeitrag(email, beitrag, verfasser);
    }

    public List<PinwandBeitrag> getBeitraegeFromPinwand(String email) {
        return pinwandRepository.getBeitraege(email);
    }
}
