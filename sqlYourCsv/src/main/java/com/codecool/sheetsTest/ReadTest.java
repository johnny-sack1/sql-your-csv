package com.codecool.sheetsTest;

public class ReadTest {

    public static void main(String[] args) {
        SheetsDataProvider dataProvider = new SheetsDataProvider();
        String query = "SELECT col1, col2 FROM 1G25HHXRj-CGO_Pvpt9VDf4ZgwFtUYGrRRFPBhBfDc7c;";
        System.out.println(dataProvider.query(query));
    }
}
