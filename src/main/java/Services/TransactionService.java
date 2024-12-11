package Services;

import Exceptions.BalanceExceededException;
import Exceptions.NegativeTransactionException;
import Exceptions.UserNotFoundException;
import Repository.UserRepository;
import models.Transaction;
import models.User;

import java.util.List;

public class TransactionService {
    private final RegexService regexService;
    private final UserRepository userRepository;
    private final CSVService csvService;
    private User user;


    public TransactionService(RegexService regexService, User user, UserRepository userRepository, CSVService csvService) {
        this.regexService = regexService;
        this.user = user;
        this.userRepository = userRepository;
        this.csvService = csvService;
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

    public void transactionToUserCSV(String fileName) {
        List<Transaction> transactions = csvService.readCSV(fileName);
        User receiver = userRepository.findUserById(transactions.get(0).getReceiverId());
        double balance = transactions.get(0).getAmount();
        transactionToUser(receiver.getEmail(), balance);
        //2. Send Transaction


    }



}
