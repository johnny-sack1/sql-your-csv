package com.codecool.sqlyourcsv.dataProvider;

import com.codecool.sqlyourcsv.exception.InvalidQueryException;
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

    boolean hasWhereClause(String query) {
        String regex = "(?i:SELECT .+ WHERE .+;)";
        return query.matches(regex);
    }

    boolean hasAndClause(String query) {
        String regex = "(?i:SELECT .+ WHERE .+ AND .+;)";
        return query.matches(regex);
    }

    boolean hasOrClause(String query) {
        String regex = "(?i:SELECT .+ WHERE .+ OR .+;)";
        return query.matches(regex);
    }

    List<String> parseAndClause(String query) {
        String regex = "(?i:AND (\\S+) (<|>|=|<>|LIKE) (\\S+);)";
        return getRegexGroups(query, regex);
    }

    List<String> parseOrClause(String query) {
        String regex = "(?i:OR (\\S+) (<|>|=|<>|LIKE) (\\S+);)";
        return getRegexGroups(query, regex);
    }

    List<String> parseWhereClause(String query) {
        String regex = "(?i:WHERE (\\S+) (<|>|=|<>|LIKE) (\\S+?)[ ;])";
        return getRegexGroups(query, regex);
    }

    private List<String> getRegexGroups(String query, String regex) {
        List<String> regexGroups = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            regexGroups.add(matcher.group(1));
            regexGroups.add(matcher.group(2));
            regexGroups.add(matcher.group(3));
        }
        return regexGroups;
    }
}
