package de.hsw.db;

import java.util.regex.Pattern;

public class RegexService {

    public RegexService() {}

    public boolean isValidEmail(String input) {
        if (input == null) {
            return false;
        }
        return input.matches("^[A-Za-z0+9+_.-]+@(.+)$");
    }

    //TODO
    public void validateCsv() {}

    public int returnCsvIndex(int index) {return index;}


}
