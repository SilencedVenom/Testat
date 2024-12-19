package Services;

import Repository.UserRepository;
import models.Transaction;
import models.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVServiceTest {

    private final UserRepository userRepository = new UserRepository();
    private CSVService csvService;
    private User testUser;

    @AfterAll
    static void tearDown() {
        UserRepository userRepository = new UserRepository();
        userRepository.deleteUserByEmail("sender@example.com");
        userRepository.deleteUserByEmail("receiver@example.com");
        System.out.println("User gelöscht");
    }

    @BeforeEach
    void seUpDatabse() {
        UserRepository userRepository = new UserRepository();
        if (!userRepository.checkIfAccountExists("sender@example.com")) {
            userRepository.addUser("sender@example.com", "password123", 1000.0, new Timestamp(System.currentTimeMillis()));
        }

        if (!userRepository.checkIfAccountExists("receiver@example.com")) {
            userRepository.addUser("receiver@example.com", "password123", 1000.0, new Timestamp(System.currentTimeMillis()));
        }
        csvService = new CSVService(testUser);
    }

    @Test
    void testReadCSV_withSinglePattern() throws IOException {
        User testUser = userRepository.findUserByEmail("sender@example.com");
        String csvFile = "./CSV/test_single.csv";
        String csvContent = "2024-06-14;receiver@example.com;sender@example.com;Test-Transaktion;150.75";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(csvContent);
        }

        List<Transaction> list = csvService.readCSV("./CSV/test_single.csv");

        assertEquals(1, list.size(), "Eine Transaktion muss vorhanden sein.");
        Transaction transaction = list.get(0);
        assertEquals(testUser.getId(), transaction.getSenderId(), "senderId muss gleich sein.");
        assertTrue(transaction.getReceiverId() > 0, "receiverId muss eine Zahl sein, die größer als 0 ist.");
        assertEquals(150.75, transaction.getAmount(), "Der Betrag muss gleich sein.");
        assertEquals("Test-Transaktion", transaction.getDescription(), "Der Inhalt der Nachricht muss der gleiche sein.");
    }

    @Test
    void testWriteCSVTransaction() {
        int senderId = userRepository.findUserByEmail("sender@example.com").getId();
        int receiverId = userRepository.findUserByEmail("receiver@example.com").getId();

        List<Transaction> list = List.of(
                new Transaction(senderId, receiverId, 200.0, "Test", new Date(System.currentTimeMillis()))
        );

        String outputFileName = "output";

        csvService.writeCSVTransactions(outputFileName, list);
        assertTrue(new java.io.File(outputFileName).exists());
    }

}