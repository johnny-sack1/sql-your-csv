package com.codecool.sheetsTest;

import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetsDataProvider {
    private SheetsQueryParser queryParser = new SheetsQueryParser();
    private Sheets sheetsService;
    private String SPREADSHEET_ID;

    private void establishConnection() {
        try {
            this.sheetsService = SheetsServiceUtil.getSheetsService();
        }
        catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public String query(String query) {
        validateQuery(query);
        SPREADSHEET_ID = queryParser.getSheetId(query);
        establishConnection();

        List<String> colsToDisplay = getColsToDisplay(query);
        Map<String, String> colsAndLetters = getSheetColsMap();

        List<String> ranges = new ArrayList<>();

        for (String col : colsToDisplay) {
            String colLetter = colsAndLetters.get(col);
            ranges.add(colLetter + ":" + colLetter);
        }

        StringBuilder requestedData = new StringBuilder();

        BatchGetValuesResponse readResult = null;
        try {
            readResult = sheetsService.spreadsheets().values()
                    .batchGet(SPREADSHEET_ID)
                    .setMajorDimension("COLUMNS")
                    .setRanges(ranges)
                    .execute();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (ValueRange colVr : readResult.getValueRanges()) {
            StringBuilder columnContents = new StringBuilder();
            for (Object cell : colVr.getValues().get(0)) {
                columnContents.append((String) cell);
                columnContents.append("\n");
            }
            requestedData.append(columnContents.toString());
        }

        return requestedData.toString();
    }

    private void validateQuery(String query) {
        try {
            queryParser.validate(query);
        } catch (InvalidQueryException e) {
            e.printStackTrace();
        }
    }

    private List<String> getColsToDisplay(String query) {
        List<String> colsToDisplay = queryParser.getColsToDisplay(query);
        if (colsToDisplay.size() == 1 && colsToDisplay.get(0).equals("*")) {
            colsToDisplay = getSheetCols();
        }
        return colsToDisplay;
    }


    private List<String> getSheetCols() {
        List<String> cols = new ArrayList<>();
        ValueRange colsVr = null;
        String range = "1:1";
        try {
            colsVr = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, range)
                    .setMajorDimension("ROWS")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Object colObj : colsVr.getValues().get(0)) {
            cols.add((String) colObj);
        }

        return cols;
    }

    private Map<String, String> getSheetColsMap() {
        List<String> cols = getSheetCols();
        Map<String, String> colsAndLetters = new HashMap<>();
        char c = 'A';
        for (String col : cols) {
            colsAndLetters.put(col, Character.toString(c));
            c++;
        }
        return colsAndLetters;
    }
}
