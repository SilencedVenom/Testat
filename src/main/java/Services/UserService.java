package Services;

import Repository.UserRepository;
import models.User;

import java.sql.Timestamp;

public class UserService {
    private final RegexService regexService;
    private final UserRepository userRepository;

    public UserService(RegexService regexService) {
        this.regexService = regexService;
        this.userRepository = new UserRepository();
    }

    public boolean checkIfAccountExists(String email) {
        User user = userRepository.findUserByEmail(email);
        return user != null;
    }

    /**
     *
     * @param email
     * @param password
     */
    public void registerUser(String email, String password) {
        if (checkIfAccountExists(email)) {
            new RuntimeException("Benutzer existiert bereits.");
        } else {
            User user = new User(-1, email, password, 1000, new Timestamp(System.currentTimeMillis()));
            userRepository.addUser(user);
        }
    }

    public double showBalance(String email) {
        User user = userRepository.findUserByEmail(email);
        return user.getBalance();

    }

}
