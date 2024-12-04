package Services;

import Exceptions.UserNotFoundException;
import Repository.UserRepository;
import models.Transaction;
import models.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class CSVService {

    private static final Pattern csvSinglePattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2};[^;]+;[^;]+;[^;]+;\\d+\\.\\d{2}$");
    private static final Pattern csvMultiPattern = Pattern.compile("^[^;]+;\\d+\\.\\d{2};[^;]+$");
    private final User user;
    public UserRepository userRepository = new UserRepository();

    public CSVService(User user) {
        this.user = user;
    }

    public List<Transaction> readCSV(String fileName) {
        String filePath = "./CSV/" + fileName + ".csv";
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (csvSinglePattern.matcher(line).matches()) {
                    singlePattern(transactions, line);
                } else if (csvMultiPattern.matcher(line).matches()) {
                    multiPattern(transactions, line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public void singlePattern(List<Transaction> transactions, String line) {
        String[] columns = line.split(";");
        Date date = Date.valueOf(columns[0]);
        String receiverEmail = columns[1];
        String senderEmail = columns[2];
        String description = columns[3];
        double amount = Double.parseDouble(columns[4]);

        User receiverUser = userRepository.findUserByEmail(receiverEmail);
        if (receiverUser == null) {
            throw new UserNotFoundException("User " + receiverEmail + " wurde nicht gefunden.");
        }

        User senderUser = userRepository.findUserByEmail(senderEmail);
        if (senderUser == null) {
            throw new UserNotFoundException("User " + senderEmail + " wurde nicht gefunden.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Betrag muss größer als 0 sein.");
        }

        transactions.add(new Transaction(senderUser.getId(), receiverUser.getId(), amount, description, date));
    }

    public void multiPattern(List<Transaction> transactions, String line) {
        String[] columns = line.split(";");
        String receiverEmail = columns[0];
        double amount = Double.parseDouble(columns[1]);
        String description = columns[2];

        User receiverUser = userRepository.findUserByEmail(receiverEmail);
        if (receiverUser == null) {
            throw new UserNotFoundException("User " + receiverEmail + " wurde nicht gefunden.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Betrag muss größer als 0 sein.");
        }

        int senderId = user.getId();

        transactions.add(new Transaction(senderId, receiverUser.getId(), amount, description, new Date(System.currentTimeMillis())));

    }
}
