package de.hsw;

import Exceptions.BalanceExceededException;
import Exceptions.NegativeTransactionException;
import Exceptions.UserNotFoundException;
import Repository.UserRepository;
import Services.CSVService;
import Services.RegexService;
import Services.TransactionService;
import Services.UserService;
import models.User;

import java.sql.Timestamp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Aktuelles Arbeitsverzeichnis: " + System.getProperty("user.dir"));
      
/*
        CSVService csvService = new CSVService();
        csvService.readCSV("test");
*/

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
                    registerSuccesfull= register(email,password);
                }

            }

            case 1 -> {
                boolean loginSuccesfull = false;
                while (!loginSuccesfull) {
                    System.out.println("Email:");
                    String email = scanner.nextLine();
                    System.out.println("Passwort:");
                    String password = scanner.nextLine();
                    loginSuccesfull= login(email,password);
                }


            }

            default -> {
                System.out.println("Ungültige Eingabe. Bitte wähle 0 oder 1.");
            }
        }
        scanner.close();

    }
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
    public static boolean register(String email, String password) {
        UserRepository userRepository = new UserRepository();
        RegexService regexService = new RegexService();
        if (userRepository.findUserByEmail(email)==null) {
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
        }else {
            System.out.println("Ein Account mit dieser E-Mail existiert bereits");
            return false;
        }






    }

}
