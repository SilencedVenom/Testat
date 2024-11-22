package de.hsw.db;

public class Validator {
    public Validator() {}

    public boolean isNotNullOrEmpty(String input) {
        return input != null && !input.isEmpty();
    }
}
