package com.codecool.queryParser;

public class InvalidQueryException extends Exception {
    public InvalidQueryException() {
        super("Invalid query");
    }
}
