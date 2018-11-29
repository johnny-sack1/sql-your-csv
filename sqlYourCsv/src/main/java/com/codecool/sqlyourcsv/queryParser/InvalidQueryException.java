package com.codecool.sqlyourcsv.queryParser;

public class InvalidQueryException extends Exception {
    public InvalidQueryException() {
        super("Invalid query");
    }
}
