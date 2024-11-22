package de.hsw.db;

import java.util.regex.Pattern;

public class RegexService {

    public RegexService() {}

    public Pattern createPattern(String regex) {
        return Pattern.compile(regex);
    }

    public boolean checkRegex(Pattern pattern, String input) {
        return pattern.matcher(input).matches();
    }
}
