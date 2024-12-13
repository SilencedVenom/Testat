package Services;

import Exceptions.BalanceExceededException;
import Exceptions.NegativeTransactionException;
import Exceptions.TransactionsNotFoundException;
import Exceptions.UserNotFoundException;
import Repository.TransactionRepository;
import Repository.UserRepository;
import models.Transaction;
import models.User;

import java.util.List;

public class TransactionService {
    private final RegexService regexService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CSVService csvService;
    private final User user;


    public TransactionService(User user) {
        this.regexService = new RegexService();
        this.user = user;
        this.userRepository = new UserRepository();
        this.csvService = new CSVService(user);
        this.transactionRepository = new TransactionRepository();
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
        if (regexService.isValidFilename(fileName)) {
            List<Transaction> transactions = csvService.readCSV(fileName);


            for (Transaction transaction : transactions) {
                User receiver = userRepository.findUserById(transaction.getReceiverId());
                if (receiver == null) {
                    throw new UserNotFoundException("Empfänger nicht gefunden");
                }
                double balance = transaction.getAmount();
                transactionToUser(receiver.getEmail(), balance);
            }

            transactionRepository.sendTransaction(transactions);
        } else {
            throw new IllegalArgumentException("Der Filename ist ist nicht gültig.");
        }
    }

    public void withdrawMoney(double balance) {
        if (balance > user.getBalance()) {
            throw new BalanceExceededException("Die zu überweisende Geldsumme übersteigt deinen Kontostand.");
        } else {
            if (balance >= 0) {
                user.setBalance(user.getBalance() - balance);
                System.out.println("Sie haben " + balance + "€ abgehoben.");
            } else {
                System.out.println("Sie können keine negativen Abbuchungen durchführen");
            }
        }
    }

    public void writeTransactions(String fileName) {
        List<Transaction> transactions = transactionRepository.getTransactionsBySenderId(user.getId());
        if (transactions.isEmpty()) {
            throw new TransactionsNotFoundException("Es sind keine Transaktionen vorhanden.");
        }
        if (regexService.isValidFilename(fileName)) {
            csvService.writeCSVTransactions(fileName, transactions);
        } else {
            throw new IllegalArgumentException("Der Filename ist ist nicht gültig.");
        }
    }


}
