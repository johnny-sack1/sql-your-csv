package com.codecool.queryParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    public void parse(String query) throws FileNotFoundException {
        try {
            validate(query);
        } catch (InvalidQueryException e) {
            e.printStackTrace();
        }
        
        String fileName = getFileName(query);

        if (!csvExists(fileName)) {
            throw new FileNotFoundException();
        }


    }

    private void validate(String query) throws InvalidQueryException {
        String regex = "(?i:SELECT \\S+ FROM \\w+\\.csv( WHERE \\w+)?;)";

        if (!query.matches(regex)) {
            throw new InvalidQueryException();
        }
    }

    private boolean csvExists(String fileName) {
        File f = new File(fileName);

        return f.exists() && !f.isDirectory();
    }

    private String getFileName(String query) {
        String regex = "(\\w+?)\\.csv";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        return matcher.group(1);
    }
}
