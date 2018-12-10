package com.codecool.sqlyourcsv.dataProvider;

import com.codecool.sqlyourcsv.model.Entry;
import com.codecool.sqlyourcsv.model.Table;
import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryExecutor {
    private SheetsQueryParser parser;
    private Table table;
    private String query;

    public QueryExecutor(Table table, String query) {
        this.table = table;
        this.query = query;
        this.parser = new SheetsQueryParser();
    }

    public Table execute() {
        try {
            parser.validate(query);
        }
        catch (InvalidQueryException e) {
            e.printStackTrace();
        }
        List<String> colsToDisplay = parser.getColsToDisplay(query);
        if (parser.hasWhereClause(query)) {
            Table restrictedTable = getContentWithWhereClause();
            return getContentNoWhereClause(restrictedTable, colsToDisplay);
        }
        else {
            return getContentNoWhereClause(table, colsToDisplay);
        }
    }

    private Table getContentNoWhereClause(Table table, List<String> colsToDisplay) {
        if (colsToDisplay.size() == 1 && colsToDisplay.get(0).equals("*")) {
            return table;
        }
        else {
            Entry headers = new Entry(colsToDisplay.toArray(new String[0]));
            List<Entry> content = new ArrayList<>();

            for (Entry entry : table.getTableContent()) {
                List<String> entryContent = new ArrayList<>();
                for (String colName : colsToDisplay) {
                    int colIndex = table.findColumnIndex(colName);
                    entryContent.add(entry.getFieldByColIndex(colIndex));
                }
                Entry newEntry = new Entry(entryContent.toArray(new String[0]));
                content.add(newEntry);
            }
            return new Table(headers, content);
        }
    }

    private Table getContentWithWhereClause() {
        List<String> parsedWhereClause = parser.parseWhereClause(query);
        Table tableAfterWhereParsing = getContentPureWhereClause(parsedWhereClause);

        if (parser.hasAndClause(query)) {
            // to implement
        }
        else if (parser.hasOrClause(query)) {
            Table tableAfterOrParsing = getContentPureWhereClause(parser.parseOrClause(query));
            tableAfterWhereParsing.join(tableAfterOrParsing);
        }

        return tableAfterWhereParsing;
    }

    private Table getContentPureWhereClause(List<String> parsedWhereClause) {
        String colName = parsedWhereClause.get(0);
        String operator = parsedWhereClause.get(1);
        String value = parsedWhereClause.get(2).replaceAll("^\"|\"$", "");

        List<Entry> newTableContent = null;
        int colIndex = table.findColumnIndex(colName);

        switch (operator.toUpperCase()) {
            case "<":
                newTableContent = table.getTableContent()
                        .stream()
                        .filter(row -> Integer.parseInt(row.getFieldByColIndex(colIndex)) < Integer.parseInt(value))
                        .collect(Collectors.toList());
                break;
            case ">":
                newTableContent = table.getTableContent()
                        .stream()
                        .filter(row -> Integer.parseInt(row.getFieldByColIndex(colIndex)) > Integer.parseInt(value))
                        .collect(Collectors.toList());
                break;
            case "=":
                newTableContent = table.getTableContent()
                        .stream()
                        .filter(row -> row.getFieldByColIndex(colIndex).equals(value))
                        .collect(Collectors.toList());
                break;
            case "<>":
                newTableContent = table.getTableContent()
                        .stream()
                        .filter(row -> !row.getFieldByColIndex(colIndex).equals(value))
                        .collect(Collectors.toList());
                break;
            case "LIKE":

                String regex = "(?i:" +
                        value.replaceAll("%", ".+").replaceAll("_", ".") +
                        ")";
                newTableContent = table.getTableContent()
                        .stream()
                        .filter(row -> row.getFieldByColIndex(colIndex).matches(regex))
                        .collect(Collectors.toList());
        }

        return new Table(table.getHeaders(), newTableContent);
    }
}
