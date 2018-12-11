package com.codecool.sqlyourcsv.filter;

import com.codecool.sqlyourcsv.model.Entry;
import com.codecool.sqlyourcsv.model.Table;
import java.util.List;

public interface Operation {
    List<Entry> performOperation(Table table, int colIndex, String value);
}
