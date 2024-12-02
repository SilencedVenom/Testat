package Services;

public class RegexService {

    public RegexService() {}

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

    //TODO
    public void validateCsv() {}

    public int returnCsvIndex(int index) {return index;}


}
