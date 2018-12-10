package com.codecool.sqlyourcsv.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Table {

    private Entry headers;
    private List<Entry> tableContent;

    public Table(Entry headers, List<Entry> content) {
        this.headers = headers;
        this.tableContent = content;
    }

    public List<String> get(String s) {
        // TODO: 29/11/2018 Implement
        return null;
    }

    public int findColumnIndex(String s) {
        String column = Stream.of(this.headers.getContent())
            .filter(w -> w.equalsIgnoreCase(s))
            .findFirst()
            .orElse("empty result");

        return Arrays.asList(this.headers.getContent()).indexOf(column);
    }

    public Entry getHeaders() {
        return this.headers;
    }

    public void setHeaders(Entry headers) {
        this.headers = headers;
    }

    public List<Entry> getTableContent() {
        return this.tableContent;
    }

    public void setTableContent(List<Entry> tableContent) {
        this.tableContent = tableContent;
    }

    public void join(Table table) {
        for (Entry entry : table.getTableContent()) {
            if (!hasEntry(entry)) {
                addEntry(entry);
            }
        }
    }

    private boolean hasEntry(Entry entry) {
        for (Entry tableEntry : this.tableContent) {
            if (tableEntry.equals(entry)) {
                return true;
            }
        }
        return false;
    }

    private void addEntry(Entry entry) {
        this.tableContent.add(entry);
    }
}
