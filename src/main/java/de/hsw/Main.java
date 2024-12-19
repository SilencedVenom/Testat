package de.hsw;

import Exceptions.TransactionsNotFoundException;
import Exceptions.UserNotFoundException;
import Repository.UserRepository;
import Services.*;
import models.User;

import java.sql.Timestamp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Aktuelles Arbeitsverzeichnis: " + System.getProperty("user.dir"));
        User currentUser = new User();
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                Möchtest du dich registrieren: 0
                Möchtest du dich anmelden: 1
                """);

        int pointer = scanner.nextInt();
        scanner.nextLine();

        switch (pointer) {
            case 0 -> {
                boolean registerSuccesfull = false;
                while (!registerSuccesfull) {
                    System.out.println("Email:");
                    String email = scanner.nextLine();
                    System.out.println("Passwort:");
                    String password = scanner.nextLine();
                    registerSuccesfull = register(email, password);
                }

            }

            case 1 -> {
                boolean loginSuccesfull = false;
                while (!loginSuccesfull) {
                    System.out.println("Email:");
                    String email = scanner.nextLine();
                    System.out.println("Passwort:");
                    String password = scanner.nextLine();
                    loginSuccesfull = login(email, password);
                    UserRepository userRepository = new UserRepository();
                    currentUser = userRepository.findUserByEmail(email);
                }
                boolean programmRuning = true;

                PinwandService pinwandService = new PinwandService();
                TransactionService transactionService = new TransactionService(currentUser);
                UserService userService = new UserService(new RegexService());
                RegexService regexService = new RegexService();
                // CSVService-Instanz erstellen
                CSVService csvService = new CSVService(currentUser);

                while (programmRuning) {
                    System.out.println("""
                            Was wollen Sie als nächstes tun?
                                        Menü:
                                        1. (Platzhalter für zukünftige Funktion)
                                        2. (Platzhalter für zukünftige Funktion)
                                        3. Guthaben einsehen
                                        4. Massenüberweisung durchführen
                                        5. Einzelüberweisung durchführen
                                        6. Geld abheben
                                        7. Pinwand Beitrag erstellen
                                        8. Einsicht in meine Pinwand
                                        9. Benutzer suchen, Pinnwand anzeigen oder Nachricht senden
                                        10. Nachrichten anzeigen
                                        11. Direktnachrichten oder Pinnwand exportieren
                                        12. Transaktionen exportieren
                                        13. Letzte 10 Transaktionen anzeigen
                                        0. Programm beenden
                            """);
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Zeilenumbruch konsumieren

                    switch (choice) {
                        case 1 -> {
                            // Platzhalter für Option 1
                            System.out.println("Option 1 ausgewählt.");
                        }
                        case 2 -> {
                            // Platzhalter für Option 2
                            System.out.println("Option 2 ausgewählt.");
                        }
                        case 3 -> {
                            //Guthaben einsehen
                            System.out.println("Use Case 3 ausgewählt.");
                            currentUser.showMyBalance();
                        }
                        case 4, 5 -> {
                            //Massenüberweisung und Einzelüberweisung
                            System.out.println("Geben Sie einen Dateinamen an ohne (.csv), welche sich in dem Ordner \"CSV\" befindet");
                            String fileName = scanner.nextLine();
                            try {
                                transactionService.transactionToUserCSV(fileName);
                            } catch (UserNotFoundException | IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        case 6 -> {
                            // Geldabheben entfernt den Betrag vom Betrag in der Datenbank und zeigt anschließend den aktualisierten Kontostand
                            System.out.println("Use Case 6 ausgewählt.");
                            System.out.println("Wieviel Geld wollen sie abheben?");
                            double geldbetrag = scanner.nextDouble();
                            transactionService.withdrawMoney(geldbetrag);
                            currentUser.showMyBalance();
                        }
                        case 7 -> {
                            // 1. Sucht User 2. Abfrage Was an die Pinwand Soll Anschließend neuen Pinwandeintrag generiert
                            System.out.println("Use Case 7 ausgewählt.");
                            boolean inputCorrect = false;
                            String input = "";
                            while(!inputCorrect) {
                                System.out.println("An wessen pinwand wollen sie schreiben?");

                                String userInput = scanner.nextLine();
                                inputCorrect = regexService.isValidEmail(userInput);
                                input = userInput;
                            }
                            User foundUser = currentUser.findUser(input);
                            if (foundUser != null) {
                                PinwandService pinwand = new PinwandService();
                                System.out.println("Was wollen sie an seine Pinwand schreiben?");
                                String pinwandbeitrag = scanner.nextLine();
                                pinwand.addBeitragToPinwand(foundUser.getEmail(), pinwandbeitrag, currentUser.getEmail());
                                System.out.println("Pinwand eintrag wurde erstellt");

                            }

                        }
                        case 8 -> {
                            // Zeigt die eigene Pinwand
                            System.out.println("Use Case 8 ausgewählt.");
                            currentUser.showPinwand();
                        }
                        case 9 -> {
                            // Use Case 9 - Benutzer suchen, Pinnwand anzeigen oder Nachricht senden
                            System.out.println("Use Case 9 ausgewählt.");
                            System.out.println("Geben Sie die E-Mail-Adresse des Benutzers ein:");
                            String email = scanner.nextLine();

                            // Suche nach einem Benutzer basierend auf der E-Mail
                            User foundUser = currentUser.findUser(email); // Aktueller Benutzer sucht nach einem anderen Benutzer
                            if (foundUser != null) {
                                System.out.println("Benutzer gefunden: " + foundUser.getEmail());
                                System.out.println("""
                                        Was möchten Sie tun?
                                        1. Pinnwand anzeigen
                                        2. Nachricht senden
                                        """);
                                int action = scanner.nextInt();
                                scanner.nextLine(); // Zeilenumbruch konsumieren

                                switch (action) {
                                    case 1 -> {
                                        // Pinnwand anzeigen
                                        foundUser.showPinwand();
                                    }
                                    case 2 -> {
                                        // Nachricht senden
                                        System.out.println("Nachricht eingeben:");
                                        String messageText = scanner.nextLine();
                                        currentUser.sendMessage(foundUser, messageText);
                                    }
                                    default -> {
                                        System.out.println("Ungültige Auswahl. Bitte wählen Sie eine gültige Option.");
                                    }
                                }
                            } else {
                                System.out.println("Benutzer mit dieser E-Mail-Adresse wurde nicht gefunden.");
                            }
                        }
                        case 10 -> {
                            // Warum wird hier nochmal gepprüft ob man eingeloggt ist? Man Kommt da gar nicht hin wenn man nicht eingeloggt ist
                            System.out.println("Use Case 10 ausgewählt.");
                            if (currentUser != null) {
                                UserRepository userRepository = new UserRepository();
                                userRepository.showMyMessages(currentUser.getEmail());
                            } else {
                                System.out.println("Sie müssen angemeldet sein, um Nachrichten anzuzeigen.");
                            }
                        }
                        case 11 -> {
                            // Abfrage des Kontaktes mit dem der Verlauf exportiert werden soll
                            System.out.println("Geben Sie die E-Mail-Adresse des Kontakts ein:");
                            String contactEmail = scanner.nextLine();
                            System.out.println("""
                                                    Was möchten Sie exportieren?
                                                    1. Direktnachrichten
                                                    2. Pinnwandbeiträge
                                                    3. Beides
                                                    """);
                            int exportChoice = scanner.nextInt();
                            scanner.nextLine(); // Zeilenumbruch konsumieren
                            System.out.println("Geben Sie den Dateinamen für den Export ein (ohne Erweiterung):");
                            String fileName = scanner.nextLine();



                            try {
                                switch (exportChoice) {
                                    case 1 -> {
                                        // Nur Direktnachrichten exportieren
                                        csvService.exportDirectMessages(contactEmail, fileName);
                                        System.out.println("Direktnachrichten erfolgreich exportiert! Datei gespeichert als: ./CSV/" + fileName + ".csv");
                                    }
                                    case 2 -> {
                                        // Nur Pinnwandbeiträge exportieren
                                        csvService.exportPinwandBeitraege(contactEmail, fileName);
                                        System.out.println("Pinnwandbeiträge erfolgreich exportiert! Datei gespeichert als: ./CSV/" + fileName + ".csv");
                                    }
                                    case 3 -> {
                                        // Beides exportieren
                                        csvService.exportMessagesAndPinwand(contactEmail, fileName);
                                        System.out.println("Direktnachrichten und Pinnwandbeiträge erfolgreich exportiert! Datei gespeichert als: ./CSV/" + fileName + ".csv");
                                    }
                                    default -> System.out.println("Ungültige Auswahl. Bitte wählen Sie 1, 2 oder 3.");
                                }
                            } catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        case 12 -> {
                            System.out.println("Bitte gib einen Dateinamen an. Die Datei wird in dem Ordner CSV gespeichert.");
                            String fileName = scanner.nextLine();
                            try {
                                transactionService.writeTransactions(fileName);
                            } catch (TransactionsNotFoundException | IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }

                        case 13 -> {
                            // Die letzten 10 Transactions werden angezeigt
                            System.out.println("Use Case 13 ausgewählt.");
                            if (currentUser != null) {
                                UserRepository userRepository = new UserRepository();
                                System.out.println("Die letzten 10 Transaktionen für Benutzer-ID: " + currentUser.getId());
                                userRepository.printLastTenTransactions(currentUser.getId());
                            } else {
                                System.out.println("Sie müssen angemeldet sein, um Ihre Transaktionen anzuzeigen.");
                            }
                        }


                        case 0 -> {
                            // Programm beenden
                            System.out.println("Programm wird beendet. Auf Wiedersehen!");
                            programmRuning = false;
                        }
                        default -> {
                            System.out.println("Ungültige Auswahl. Bitte wählen Sie eine gültige Option.");
                        }
                    }
                }
            }

            default -> {
                System.out.println("Ungültige Eingabe. Bitte wähle 0 oder 1.");
            }
        }
        scanner.close();

    }
    // Loginmethode überprüft ob das Passwort mit dem in der Datenbank übereinstimmt
    public static boolean login(String email, String password) {
        UserRepository userRepository = new UserRepository();
        User user = userRepository.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login erfolgreich. Willkommen " + user.getEmail());
            return true;

        } else if (user == null) {
            System.out.println("Benutzer nicht gefunden.");
            return false;
        } else {
            System.out.println("Falsches Passwort.");
            return false;
        }
    }
    // Registrierung
    public static boolean register(String email, String password) {
        UserRepository userRepository = new UserRepository();
        RegexService regexService = new RegexService();
        if (userRepository.findUserByEmail(email) == null) {
            if (regexService.isValidEmail(email)) {
                if (regexService.isValidPassword(password)) {
                    userRepository.addUser(email, password, 1000, new Timestamp(System.currentTimeMillis()));
                    System.out.println("Registrierung erfolgreich!");
                    return true;
                } else {
                    System.out.println("Das Passwort-Format ist nicht korrekt.");
                    return false;
                }
            } else {
                System.out.println("E-Mail ist nicht im richtigen Format. Versuche es erneut.");
                return false;
            }
        } else {
            System.out.println("Ein Account mit dieser E-Mail existiert bereits");
            return false;
        }


    }
}
