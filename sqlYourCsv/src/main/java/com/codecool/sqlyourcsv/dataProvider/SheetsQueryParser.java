package com.codecool.sqlyourcsv.dataProvider;

import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class SheetsQueryParser {

    void validate(String query) throws InvalidQueryException {
        String regex = "(?i:SELECT .+( WHERE \\S+ (<|>|=|<>) \\d+)?;)";

        if (!query.matches(regex)) {
            throw new InvalidQueryException();
        }
    }

    String getSheetId(String query) {
        String regex = "FROM (\\S+?)(;|WHERE)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        return matcher.group(1);
    }

    List<String> getColsToDisplay(String query) {
        //List<String> cols = new ArrayList<>();
        String regex = "(?i:SELECT (.+) WHERE .+;)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        String[] colsArr = matcher.group(1).split(", ");
        return Arrays.asList(colsArr);
    }

    public boolean hasWhereClause(String query) {
        String regex = "(?i:SELECT .+ WHERE .+;)";
        return query.matches(regex);
    }

    public List<String> parseWhereClause(String query) {
        List<String> parsedWhereClause = new ArrayList<>();
        // 1st element in this list is the name of the column being restricted
        // 2nd element is the logical operator
        // 3rd element is the restricting value

        String regex = "(?i:WHERE (\\S+) (<|>|=|<>) (\\d+);)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        parsedWhereClause.add(matcher.group(1));
        parsedWhereClause.add(matcher.group(2));
        parsedWhereClause.add(matcher.group(3));

        return parsedWhereClause;
    }
}
