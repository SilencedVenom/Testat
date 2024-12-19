package Services;

import Exceptions.BalanceExceededException;
import Exceptions.NegativeTransactionException;
import Exceptions.TransactionsNotFoundException;
import Exceptions.UserNotFoundException;
import Repository.TransactionRepository;
import Repository.UserRepository;
import models.Transaction;
import models.User;

import java.io.FileNotFoundException;
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

    /**
     * @param email   Email vom User
     * @param balance zu übertragender Betrag
     */
    private void transactionToUser(String email, double balance) {
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
        String filesPath = "./CSV/" + fileName + ".csv";
        if (regexService.isValidFilename(filesPath)) {
            try {
                List<Transaction> transactions = csvService.readCSV(filesPath);

                for (Transaction transaction : transactions) {
                    User receiver = userRepository.findUserById(transaction.getReceiverId());
                    if (receiver == null) {
                        throw new UserNotFoundException("Empfänger nicht gefunden");
                    }
                    double balance = transaction.getAmount();
                    try {
                        transactionToUser(receiver.getEmail(), balance);
                    } catch (NegativeTransactionException | UserNotFoundException | BalanceExceededException e) {
                        System.out.println(e.getMessage());
                    }
                }

                transactionRepository.sendTransaction(transactions);
                System.out.println("Die Überweisung war erfolgreich.");
            } catch (FileNotFoundException e) {
                System.out.println("Die Datei wurde nicht gefunden: " + filesPath);
            }
        } else {
            throw new IllegalArgumentException("Der Filename ist nicht gültig.");
        }
    }

    /**
     * Entnimmt den Geldbetrag vom aktuell eingeloggten Kunden
     * @param balance
     */
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
    /**
     * Erzeugt einen Filepath zur Weiterverarbietung
     * @param fileName
     */
    public void writeTransactions(String fileName) {
        String filePath = "./CSV/" + fileName + ".csv";
        List<Transaction> transactions = transactionRepository.getTransactionsBySenderId(user.getId());
        if (transactions.isEmpty()) {
            throw new TransactionsNotFoundException("Es sind keine Transaktionen vorhanden.");
        }
        if (regexService.isValidFilename(filePath)) {
            csvService.writeCSVTransactions(filePath, transactions);
        } else {
            throw new IllegalArgumentException("Der Filename ist ist nicht gültig.");
        }
    }


}
