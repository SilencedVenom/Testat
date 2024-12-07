package de.hsw;

import Repository.UserRepository;
import Services.RegexService;
import Services.UserService;
import java.sql.Timestamp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Aktuelles Arbeitsverzeichnis: " + System.getProperty("user.dir"));
/*
        CSVService csvService = new CSVService();
        csvService.readCSV("test");
*/


        UserRepository userRepository = new UserRepository();
        RegexService regexService = new RegexService();
        UserService userService = new UserService(regexService);
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                Möchtest du dich registrieren: 0
                Möchtest du dich anmelden: 1
                """);

        int pointer = scanner.nextInt();
        scanner.nextLine();

        switch (pointer) {
            case 0 -> {
                String email = "";
                String password = "";
                boolean isRunning = true;

                while (isRunning) {
                    System.out.println("Email:");
                    email = scanner.nextLine();
                    if (regexService.isValidEmail(email)) {
                        isRunning = false;
                    } else {
                        System.out.println("E-Mail ist nicht im richtigen Format. Versuche es erneut.");
                    }
                }

                isRunning = true;
                while (isRunning) {
                    System.out.println("Bitte gib ein Passwort mit mind. 8 Zeichen und min. 1 Zahl ein.");
                    System.out.println("Password:");
                    password = scanner.nextLine();
                    if (regexService.isValidPassword(password)) {
                        isRunning = false;
                    } else {
                        System.out.println("Das Passwort-Format ist nicht korrekt.");
                    }
                }

                userRepository.addUser(email, password, 1000, new Timestamp(System.currentTimeMillis()));
                System.out.println("Registrierung erfolgreich!");
            }

            case 1 -> {

            }

            default -> {
                System.out.println("Ungültige Eingabe. Bitte wähle 0 oder 1.");
            }
        }
        scanner.close();


    }
}
