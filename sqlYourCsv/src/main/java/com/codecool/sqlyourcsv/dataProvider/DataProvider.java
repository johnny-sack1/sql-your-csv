package com.codecool.sqlyourcsv.dataProvider;

import com.codecool.sqlyourcsv.model.Entry;
import com.codecool.sqlyourcsv.model.Table;
import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;
import com.codecool.sqlyourcsv.queryParser.QueryParser;
import com.codecool.sqlyourcsv.service.FileService;

import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class DataProvider implements IDataProvider {

    private QueryParser queryParser = new QueryParser();

    private FileService file = new FileService();

    public String query(String query) {
        validateQuery(query);
        String fileName = queryParser.getFileName(query);
        Table table = getTableByFileName(fileName);

        assert table != null;

        String[] colsToDisplay = getColsToDisplay(table, query);
        String requestedContent = "";

        if (!queryParser.hasWhereClause(query)) {
            requestedContent = getContentNoWhereClause(table, colsToDisplay);
        }

        return requestedContent;
    }

    private void validateQuery(String query) {
        try {
            queryParser.validate(query);
        } catch (InvalidQueryException e) {
            e.printStackTrace();
        }
    }

    private Table getTableByFileName(String fileName) {
        Table table = null;
        try {
            table = file.getTable(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    private String[] getColsToDisplay(Table table, String query) {
        String[] colsToDisplay = queryParser.getColsToDisplay(query);
        if (colsToDisplay.length == 1 && colsToDisplay[0].equals("*")) {
            colsToDisplay = table.getHeaders().getContent();
        }

        return colsToDisplay;
    }

    private String getContentNoWhereClause(Table table, String[] colsToDisplay) {
        StringBuilder requestedContent = new StringBuilder();

        for (String colName : colsToDisplay) {
            int colIndex = table.findColumnIndex(colName);
            requestedContent.append(colName);
            requestedContent.append("\n");
            for (Entry entry : table.getTableContent()) {
                requestedContent.append(entry.getFieldByColIndex(colIndex));
                requestedContent.append("\n");
            }
        }

        return requestedContent.toString();
    }
}
