package Services;

import Repository.UserRepository;
import models.User;

public class UserService {
    private final RegexService regexService;
    private final UserRepository userRepository;

    public UserService(RegexService regexService) {
        this.regexService = regexService;
        this.userRepository = new UserRepository();
    }

    // Method to check if an account exists


    // Method to login a user

}
