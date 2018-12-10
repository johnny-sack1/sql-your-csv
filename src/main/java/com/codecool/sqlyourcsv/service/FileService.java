package com.codecool.sqlyourcsv.service;

import com.codecool.sqlyourcsv.model.Entry;
import com.codecool.sqlyourcsv.model.Table;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileService {

    public Table getTable(String tableName) throws IOException {
        String fileName = tableName + ".csv";
        Stream<String> stream = loadFileToStream(fileName);
        return  populateTable(stream);
    }

    private Table populateTable(Stream<String> stream) {
        List<Entry> tableContent = new ArrayList<>();
        stream.forEach(line -> tableContent.add(new Entry((String) line, ",")));
        Entry header = tableContent.get(0);
        tableContent.remove(0);
        return new Table(header, tableContent);
    }

    private Stream<String> loadFileToStream(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName));
    }
}
