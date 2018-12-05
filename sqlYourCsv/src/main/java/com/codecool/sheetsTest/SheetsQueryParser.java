package com.codecool.sheetsTest;

import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        public List<String> getColsToDisplay(String query) {
            List<String> cols = new ArrayList<>();
            String regex = "SELECT (.+) FROM .+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(query);
            matcher.find();
            String[] colsArr = matcher.group(1).split(", ");
            return Arrays.asList(colsArr);
        }

        public boolean hasWhereClause(String query) {
            String regex = "SELECT .+ FROM .+ WHERE .+";
            return query.matches(regex);
        }
}