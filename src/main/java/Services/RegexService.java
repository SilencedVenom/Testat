package Services;

public class RegexService {

    public RegexService() {
    }

    /**
     * Überprüft ob email Korrekt ist
     *
     * @param input
     * @return {@code true} wenn die Syntax stimmt
     */
    public boolean isValidEmail(String input) {
        if (input == null) {
            return false;
        }
        return input.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }

    /**
     * Überprüft ob Passwort den anforderungen entspricht
     *
     * @param input
     * @return {@code true} wenn die Syntax stimmt
     */
    public boolean isValidPassword(String input) {
        if (input == null) {
            return false;
        }
        return input.matches("^(?=.*\\d).{8,}$");
    }


    /**
     * Überpfüt ob input den Anforderung an Filename entspricht
     *
     * @param input
     * @return {@code true} wenn die Syntax stimmt
     */
    public boolean isValidFilename(String input) {
        if (input == null) {
            return false;
        }
        return input.matches("^(?:\\.\\/)?(?:[a-zA-Z0-9_\\-]+\\/)*[a-zA-Z0-9_\\-]+(?:\\.[a-zA-Z0-9_\\-]+)*\\.csv$");
    }
}
