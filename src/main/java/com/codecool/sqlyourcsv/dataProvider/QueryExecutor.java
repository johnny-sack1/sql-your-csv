package com.codecool.sqlyourcsv.dataProvider;

import com.codecool.sqlyourcsv.filter.Operation;
import com.codecool.sqlyourcsv.model.Entry;
import com.codecool.sqlyourcsv.model.Table;
import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class QueryExecutor {
    private SheetsQueryParser parser;
    private Table table;
    private String query;
    private HashMap<String, Operation> operations;

    public QueryExecutor(Table table, String query) {
        this.table = table;
        this.query = query;
        this.parser = new SheetsQueryParser();
        populateOperationMap();
    }

    public Table execute() throws InvalidQueryException, IndexOutOfBoundsException {
        parser.validate(query);
        List<String> colsToDisplay = parser.getColsToDisplay(query);
        if (parser.hasWhereClause(query)) {
            Table restrictedTable = getContentWithWhereClause();
            return getContentNoWhereClause(restrictedTable, colsToDisplay);
        } else {
            return getContentNoWhereClause(table, colsToDisplay);
        }
    }

    private Table getContentNoWhereClause(Table table, List<String> colsToDisplay)
        throws IndexOutOfBoundsException {
        if (colsToDisplay.size() == 1 && colsToDisplay.get(0).equals("*")) {
            return table;
        } else {
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

    private Table getContentWithWhereClause() throws IndexOutOfBoundsException {
        List<String> parsedWhereClause = parser.parseWhereClause(query);
        Table tableAfterWhereParsing = getContentPureWhereClause(table, parsedWhereClause);

        if (parser.hasAndClause(query)) {
            Table tableAfterAndParsing =
                getContentPureWhereClause(tableAfterWhereParsing, parser.parseAndClause(query));
            return tableAfterAndParsing;
        } else if (parser.hasOrClause(query)) {
            Table tableAfterOrParsing =
                getContentPureWhereClause(table, parser.parseOrClause(query));
            tableAfterWhereParsing.join(tableAfterOrParsing);
        }

        return tableAfterWhereParsing;
    }

    private Table getContentPureWhereClause(Table table, List<String> parsedWhereClause)
        throws IndexOutOfBoundsException {
        String colName = parsedWhereClause.get(0);
        String operator = parsedWhereClause.get(1);
        String value = parsedWhereClause.get(2).replaceAll("^\"|\"$", "");

        List<Entry> newTableContent = null;
        int colIndex = table.findColumnIndex(colName);

        newTableContent =
            this.operations.get(operator.toUpperCase()).performOperation(table, colIndex, value);

        return new Table(table.getHeaders(), newTableContent);
    }

    private void populateOperationMap() {
        this.operations = new HashMap<>();
        this.operations.put("<", (table, colIndex, value) -> table.getTableContent()
            .stream()
            .filter(
                row -> Integer.parseInt(row.getFieldByColIndex(colIndex)) < Integer.parseInt(value))
            .collect(Collectors.toList()));

        this.operations.put(">", (table, colIndex, value) -> table.getTableContent()
            .stream()
            .filter(row -> Integer.parseInt(row.getFieldByColIndex(colIndex))
                > Integer.parseInt(value))
            .collect(Collectors.toList()));

        this.operations.put("=", (table, colIndex, value) -> table.getTableContent()
            .stream()
            .filter(row -> row.getFieldByColIndex(colIndex).equals(value))
            .collect(Collectors.toList()));

        this.operations.put("<>", (table, colIndex, value) -> table.getTableContent()
            .stream()
            .filter(row -> !row.getFieldByColIndex(colIndex).equals(value))
            .collect(Collectors.toList()));

        this.operations.put("LIKE", (table, colIndex, value) -> {
            String regex = "(?i:" +
                value.replaceAll("%", ".+").replaceAll("_", ".") +
                ")";
            return table.getTableContent()
                .stream()
                .filter(row -> row.getFieldByColIndex(colIndex).matches(regex.toString()))
                .collect(Collectors.toList());
        });
    }
}
