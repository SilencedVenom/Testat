package Services;

import Exceptions.BalanceExceededException;
import Exceptions.NegativeTransactionException;
import Exceptions.UserNotFoundException;
import Repository.UserRepository;
import models.User;

public class TransactionService {
    public final RegexService regexService;
    public UserRepository userRepository;
    public User user;

    public TransactionService(RegexService regexService, User user, UserRepository userRepository) {
        this.regexService = regexService;
        this.user = user;
        this.userRepository = userRepository;
    }

    public void transactionToUser(String email, double balance) {
        if (balance > user.getBalance()) {
            throw new BalanceExceededException("Die zu überweisende Geldsumme übersteigt deinen Kontostand.");
        }

        if (!regexService.isValidEmail(email)) {
            throw new UserNotFoundException("Die Email ist nicht vorhanden.");
        }

        User target = userRepository.findUserByEmail(email);
        if (target == null) {
            throw new UserNotFoundException("Der Benutzer ist nicht vorhanden.");
        }

        if (balance >= 0) {
            target.setBalance(target.getBalance() + balance);
            user.setBalance(user.getBalance() - balance);
            userRepository.updateBalance(user.getEmail(), user.getBalance());
            userRepository.updateBalance(target.getEmail(), target.getBalance());
        } else {
            throw new NegativeTransactionException("Du kannst keine negative Überweisung tätigen");
        }

    }

    public void withdrawMoney(double balance) {
        if (balance > user.getBalance()) {
            throw new BalanceExceededException("Die zu überweisende Geldsumme übersteigt deinen Kontostand.");
        } else {
            if (balance >= 0) {
                user.setBalance(user.getBalance() - balance);
                System.out.println("Sie haben " + balance + "€ abgehoben.");
            } else{
                System.out.println("Sie können keine negativen Abbuchungen durchführen");
            }
        }
    }


}
