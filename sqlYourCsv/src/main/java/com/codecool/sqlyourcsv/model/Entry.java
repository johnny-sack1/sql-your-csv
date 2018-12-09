package com.codecool.sqlyourcsv.model;

import java.util.Arrays;

public class Entry {

    private final String[] content;

    public Entry(String contentString, String delimiter) {
        this.content = contentString.split(delimiter);
    }

    public Entry(String[] content) {
        this.content = content;
    }

    public String[] getContent() {
        return this.content;
    }

    public String getFieldByColIndex(int colIndex) {return content[colIndex]; }

    @Override public String toString() {
        return Arrays.toString(this.content);
    }
}
