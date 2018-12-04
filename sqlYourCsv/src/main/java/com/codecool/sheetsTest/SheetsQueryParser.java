package com.codecool.sheetsTest;

import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SheetsQueryParser {
        public void validate(String query) throws InvalidQueryException {
            String regex = "(?i:SELECT .+ FROM \\S+( WHERE \\S+ (<|>|=|<>) \\d+)?;)";

            if (!query.matches(regex)) {
                throw new InvalidQueryException();
            }
        }

        public String getSheetId(String query) {
            String regex = "FROM (\\S+?)(;|WHERE)";
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
