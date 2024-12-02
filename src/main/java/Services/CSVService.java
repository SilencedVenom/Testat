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
    public UserRepository userRepository = new UserRepository();

    public List<Transaction> readCSV(String fileName) {
        String filePath = "./CSV/" + fileName + ".csv";
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                if (!csvSinglePattern.matcher(line).matches()) {
                    System.out.println("Fehler in Zeile " + lineNumber + ": " + line);
                    lineNumber++;
                    continue;
                }

                String[] columns = line.split(";");
                Date date = Date.valueOf(columns[0]);
                String receiver = columns[1];

                User receiverUser = userRepository.findUserByEmail(receiver);
                if (receiverUser == null) {
                    throw new UserNotFoundException("User " + receiver + " ist nicht vorhanden.");
                }

                String sender = columns[2];

                User senderUser = userRepository.findUserByEmail(sender);
                if (senderUser == null) {
                    throw new UserNotFoundException("User " + receiver + " ist nicht vorhanden.");
                }

                String description = columns[3];
                double amount = Double.parseDouble(columns[4]);

                if (amount <= 0) {
                    throw new IllegalArgumentException("Betrag in Zeile " + lineNumber + " muss größer als 0 sein");
                }
                transactions.add(new Transaction(senderUser.getId(), receiverUser.getId(), amount, description, date));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
