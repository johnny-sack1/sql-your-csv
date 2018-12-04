package com.codecool.sheetsTest;

import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class SheetsDataProvider {
    private SheetsQueryParser queryParser = new SheetsQueryParser();
    private Sheets sheetsService;
    private String SPREADSHEET_ID;

    public SheetsDataProvider() {
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
        StringBuilder requestedData = new StringBuilder();

        List<String> ranges = new ArrayList<>();
        for (int i = 65; i < 75; i++) {
            String range = (char) i + ":" + (char) i;
            ranges.add(range);
        }

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
}
