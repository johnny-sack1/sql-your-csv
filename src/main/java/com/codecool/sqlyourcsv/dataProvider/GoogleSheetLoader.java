package com.codecool.sqlyourcsv.dataProvider;

import com.codecool.sqlyourcsv.model.Entry;
import com.codecool.sqlyourcsv.model.Table;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoogleSheetLoader implements IDataProvider {
    private static final String APPLICATION_NAME = "SQLYourCSV";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES =
        Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String GOOGLE_SHEET_ID = "1txZK-dnJgMJk4Cv9mkJoHv0zZgmt1HLf61BUSHVhySw";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
        throws IOException {
        InputStream in = GoogleSheetLoader.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private Table getTable(String tableName) throws IOException, GeneralSecurityException {
        List<List<Object>> values = getSheetContentForRequired(tableName);
        return parseDataFromSheetToTable(values);
    }

    public Table query(String query) throws IOException, GeneralSecurityException {
        return getTable(query);
    }

    private List<List<Object>> getSheetContentForRequired(String table)
        throws IOException, GeneralSecurityException {
        if (!table.contains("->")) {
            return GetSheetContent(table);
        }
        String id = table.split("->")[0];
        String tableName = table.split("->")[1];
        return GetSheetContent(id, tableName);
    }

    private Table parseDataFromSheetToTable(List<List<Object>> values) {
        Entry headers = parseRowToEntry(values.get(0));
        List<Entry> content = new ArrayList<>();
        for (int i = 1; i < values.size(); i++) {
            content.add(parseRowToEntry(values.get(i)));
        }
        return new Table(headers, content);
    }

    private Entry parseRowToEntry(List<Object> row) {
        List<String> entryData = new ArrayList<>();
        row.forEach(element -> entryData.add((String) element));
        return new Entry(entryData.toArray(new String[0]));
    }

    private List<List<Object>> GetSheetContent(String tableName)
        throws IOException, GeneralSecurityException {
        return handleHttpTransport(GOOGLE_SHEET_ID, tableName);
    }

    private List<List<Object>> GetSheetContent(String id, String tableName)
        throws IOException, GeneralSecurityException {
        return handleHttpTransport(id, tableName);
    }

    private List<List<Object>> handleHttpTransport(String id, String tableName)
        throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range = tableName.replace("_", " ").replace(";", "");
        Sheets service =
            new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
            .get(id, range)
            .execute();

        System.out.println(response);

        return response.getValues();
    }
}

