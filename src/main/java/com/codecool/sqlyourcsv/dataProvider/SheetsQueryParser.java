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
        String regex = "(?i:SELECT .+( WHERE \\S+ (<|>|=|<>|LIKE) \\S+)?;)";

        if (!query.matches(regex)) {
            throw new InvalidQueryException();
        }
    }

    List<String> getColsToDisplay(String query) {
        String regex = "(?i:SELECT (.+?)( WHERE|;))";
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

    public boolean hasAndClause(String query) {
        String regex = "(?i:SELECT .+ WHERE .+ AND .+;)";
        return query.matches(regex);
    }

    public boolean hasOrClause(String query) {
        String regex = "(?i:SELECT .+ WHERE .+ OR .+;)";
        return query.matches(regex);
    }

    public List<String> parseAndClause(String query) {
        List<String> parsedAndClause = new ArrayList<>();
        String regex = "(?i:AND (\\S+) (<|>|=|<>|LIKE) (\\S+);)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        parsedAndClause.add(matcher.group(1));
        parsedAndClause.add(matcher.group(2));
        parsedAndClause.add(matcher.group(3));

        return parsedAndClause;
    }

    public List<String> parseOrClause(String query) {
        List<String> parsedOrClause = new ArrayList<>();
        String regex = "(?i:OR (\\S+) (<|>|=|<>|LIKE) (\\S+);)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        parsedOrClause.add(matcher.group(1));
        parsedOrClause.add(matcher.group(2));
        parsedOrClause.add(matcher.group(3));

        return parsedOrClause;
    }

    public List<String> parseWhereClause(String query) {
        List<String> parsedWhereClause = new ArrayList<>();
        // 1st element in this list is the name of the column being restricted
        // 2nd element is the logical operator
        // 3rd element is the restricting value

        String regex = "(?i:WHERE (\\S+) (<|>|=|<>|LIKE) (\\S+?)[ ;])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        parsedWhereClause.add(matcher.group(1));
        parsedWhereClause.add(matcher.group(2));
        parsedWhereClause.add(matcher.group(3));

        return parsedWhereClause;
    }
}
