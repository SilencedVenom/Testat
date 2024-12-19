package Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexServiceTest {

    private RegexService regexService;

    @BeforeEach
    void setUp() {
        regexService = new RegexService();
    }

    @Test
    void testIsValidEmail() {
        // Gültige E-Mails
        assertTrue(regexService.isValidEmail("test@example.com"), "Valid email should return true");
        assertTrue(regexService.isValidEmail("user.name+alias@example.co.uk"), "Valid email with alias should return true");
        assertTrue(regexService.isValidEmail("user123@example.io"), "Valid email with numbers should return true");

        // Ungültige E-Mails
        assertFalse(regexService.isValidEmail("plainaddress"), "Invalid email without @ should return false");
        assertFalse(regexService.isValidEmail("@missingusername.com"), "Invalid email without username should return false");
        assertFalse(regexService.isValidEmail("user@.com.my"), "Invalid email with missing domain should return false");
        assertFalse(regexService.isValidEmail(null), "Null input should return false");
    }

    @Test
    void testIsValidPassword() {
        // Gültige Passwörter
        assertTrue(regexService.isValidPassword("Password1"), "Valid password should return true");
        assertTrue(regexService.isValidPassword("12345678"), "Valid numeric password should return true");
        assertTrue(regexService.isValidPassword("abcd1234"), "Valid alphanumeric password should return true");

        // Ungültige Passwörter
        assertFalse(regexService.isValidPassword("short1"), "Password shorter than 8 characters should return false");
        assertFalse(regexService.isValidPassword("noNumberPassword"), "Password without a digit should return false");
        assertFalse(regexService.isValidPassword(null), "Null input should return false");
    }

    @Test
    void testIsValidFilename() {
        // Gültige Dateinamen
        assertTrue(regexService.isValidFilename("file.csv"), "Valid filename should return true");
        assertTrue(regexService.isValidFilename("./file.csv"), "Valid filename with relative path should return true");
        assertTrue(regexService.isValidFilename("folder/subfolder/file.csv"), "Valid filename with nested folder should return true");

        // Ungültige Dateinamen
        assertFalse(regexService.isValidFilename("file.txt"), "Filename with wrong extension should return false");
        assertFalse(regexService.isValidFilename(""), "Empty filename should return false");
        assertFalse(regexService.isValidFilename(null), "Null input should return false");
        assertFalse(regexService.isValidFilename("filecsv"), "Filename without .csv should return false");
        assertFalse(regexService.isValidFilename("/invalid/start/file.csv"), "Filename with absolute path should return false");
    }
}
