package Services;

import Exceptions.BalanceExceededException;
import Repository.UserRepository;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionServiceTest {

    private User user;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        user = new User(1, "test@example.com", "password", 1000.0, null); // Beispielwerte
        transactionService = new TransactionService(new RegexService(), user, new UserRepository(), new CSVService());
    }

    @Test
    void withdrawMoney_Success() {
        double amountToWithdraw = 500.0;
        transactionService.withdrawMoney(amountToWithdraw);
        assertEquals(500.0, user.getBalance(), "Der Kontostand sollte nach der Abhebung korrekt sein.");
    }

    @Test
    void withdrawMoney_BalanceExceededException() {
        double amountToWithdraw = 1500.0;
        Exception exception = assertThrows(BalanceExceededException.class, () -> transactionService.withdrawMoney(amountToWithdraw));
        assertEquals("Die zu überweisende Geldsumme übersteigt deinen Kontostand.", exception.getMessage());
    }

    @Test
    void withdrawMoney_NegativeAmount() {
        double amountToWithdraw = -200.0;
        transactionService.withdrawMoney(amountToWithdraw);
        assertEquals(1000.0, user.getBalance(), "Der Kontostand sollte unverändert bleiben, da keine Abbuchung durchgeführt wurde.");
    }
}
