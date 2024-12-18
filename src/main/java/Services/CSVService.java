package Services;

import Exceptions.UserNotFoundException;
import Repository.MessageRepository;
import Repository.PinwandRepository;
import Repository.UserRepository;
import models.Message;
import models.PinwandBeitrag;
import models.Transaction;
import models.User;

import java.io.*;
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
                    try {
                        singlePattern(transactions, line);
                    } catch (UserNotFoundException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (csvMultiPattern.matcher(line).matches()) {
                    try {
                        multiPattern(transactions, line);
                    } catch (UserNotFoundException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
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

    public void writeCSVTransactions(String fileName, List<Transaction> transactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            User sender = userRepository.findUserById(transactions.get(0).getSenderId());
            for (Transaction item : transactions) {
                User receiver = userRepository.findUserById(item.getReceiverId());

                String[] row = {
                        item.getCreatedAt().toString(),
                        receiver.getEmail(),
                        sender.getEmail(),
                        item.getDescription(),
                        String.valueOf(item.getAmount()),
                };

                writer.write(String.join(";", row));
                writer.newLine();
            }

            System.out.println("CSV wurde erfolgreich geschrieben.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void exportDirectMessages(String contactEmail, String fileName) {
        String filePath = "./CSV/" + fileName + ".csv";
        MessageRepository messageRepository = new MessageRepository();

        // Kontakt validieren
        User contactUser = userRepository.findUserByEmail(contactEmail);
        if (contactUser == null) {
            throw new UserNotFoundException("Der Benutzer mit der E-Mail " + contactEmail + " wurde nicht gefunden.");
        }

        // Direktnachrichten abrufen
        List<Message> messages = messageRepository.getDirectMessages(user.getId(), contactUser.getId());

        if (messages.isEmpty()) {
            System.out.println("Keine Direktnachrichten gefunden.");
            return;
        }

        // CSV-Datei schreiben
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Header schreiben
            writer.write("Zeitpunkt der Nachricht;Sender;Empfänger;Nachricht");
            writer.newLine();

            // Nachrichten schreiben
            for (Message message : messages) {
                String row = String.join(";",
                        message.getCreatedAt().toString(),
                        userRepository.findUserById(message.getSenderId()).getEmail(),
                        userRepository.findUserById(message.getReceiverId()).getEmail(),
                        message.getMessage()
                );
                writer.write(row);
                writer.newLine();
            }

            System.out.println("Direktnachrichten erfolgreich exportiert: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der CSV-Datei", e);
        }
    }
    public void exportPinwandBeitraege(String contactEmail, String fileName) {
        String filePath = "./CSV/" + fileName + ".csv";
        PinwandRepository pinwandRepository = new PinwandRepository();

        // Beiträge abrufen
        List<PinwandBeitrag> beitraege = pinwandRepository.getBeitraegeByVerfasser(contactEmail, user.getEmail());

        if (beitraege.isEmpty()) {
            System.out.println("Keine Pinnwandbeiträge gefunden.");
            return;
        }

        // CSV-Datei schreiben
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Header schreiben
            writer.write("Zeitpunkt der Nachricht;Empfänger (Pinnwand);Verfasser;Nachricht");
            writer.newLine();

            // Beiträge schreiben
            for (PinwandBeitrag beitrag : beitraege) {
                String row = String.join(";",
                        beitrag.getTimestamp().toString(),
                        beitrag.getEmail(),
                        beitrag.getVerfasser(),
                        beitrag.getBeitrag()
                );
                writer.write(row);
                writer.newLine();
            }

            System.out.println("Pinnwandbeiträge erfolgreich exportiert: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der CSV-Datei", e);
        }
    }

    public void exportMessagesAndPinwand(String contactEmail, String fileName) {
        String filePath = "./CSV/" + fileName + ".csv";
        MessageRepository messageRepository = new MessageRepository();
        PinwandRepository pinwandRepository = new PinwandRepository();

        // Kontakt validieren
        User contactUser = userRepository.findUserByEmail(contactEmail);
        if (contactUser == null) {
            throw new UserNotFoundException("Der Benutzer mit der E-Mail " + contactEmail + " wurde nicht gefunden.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Header schreiben
            writer.write("Zeitpunkt der Nachricht;Sender;Empfänger;Nachricht");
            writer.newLine();

            // 1. Direktnachrichten abrufen und schreiben
            List<Message> directMessages = messageRepository.getDirectMessages(user.getId(), contactUser.getId());
            for (Message message : directMessages) {
                String row = String.join(";",
                        message.getCreatedAt().toString(),
                        userRepository.findUserById(message.getSenderId()).getEmail(), // Sender
                        userRepository.findUserById(message.getReceiverId()).getEmail(), // Empfänger
                        message.getMessage()
                );
                writer.write(row);
                writer.newLine();
            }

            // 2. Pinnwandbeiträge abrufen und schreiben
            List<PinwandBeitrag> pinwandBeitraege = pinwandRepository.getBeitraegeByVerfasser(contactEmail, user.getEmail());
            for (PinwandBeitrag beitrag : pinwandBeitraege) {
                String row = String.join(";",
                        beitrag.getTimestamp().toString(),
                        beitrag.getVerfasser(), // Verfasser
                        beitrag.getEmail(), // Pinnwand-Empfänger
                        beitrag.getBeitrag() // Nachricht
                );
                writer.write(row);
                writer.newLine();
            }

            System.out.println("Nachrichten und Pinnwandbeiträge wurden erfolgreich exportiert: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der CSV-Datei", e);
        }
    }
}
