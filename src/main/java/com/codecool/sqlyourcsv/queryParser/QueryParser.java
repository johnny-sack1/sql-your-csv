package com.codecool.sqlyourcsv.queryParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    public void validate(String query) throws InvalidQueryException {
        String regex = "(?i:SELECT .+ FROM \\w+\\.csv( WHERE \\w+ (<|>|=|<>) \\d+)?;)";

        if (!query.matches(regex)) {
            throw new InvalidQueryException();
        }
    }

    public String getFileName(String query) {
        String regex = "(\\w+?)\\.csv";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        return matcher.group(1);
    }

    public String[] getColsToDisplay(String query) {
        String regex = "SELECT (.+) FROM .+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        return matcher.group(1).split(", ");
    }

    public boolean hasWhereClause(String query) {
        String regex = "SELECT .+ FROM .+ WHERE .+";
        return query.matches(regex);
    }
}
