package Services;

public class RegexService {

    public RegexService() {
    }

    public boolean isValidEmail(String input) {
        if (input == null) {
            return false;
        }
        return input.matches("^[A-Za-z0+9+_.-]+@(.+)$");
    }

    public boolean isValidPassword(String input) {
        if (input == null) {
            return false;
        }
        return input.matches("^(?=.*\\d).{8,}$");
    }

    public boolean isValidFilename(String input) {
        if (input == null) {
            return false;
        }
        return input.matches("^(?:\\.\\/)?(?:[a-zA-Z0-9_\\-]+\\/)*[a-zA-Z0-9_\\-]+(?:\\.[a-zA-Z0-9_\\-]+)*\\.csv$");
    }
}
