package com.codecool.sqlyourcsv.dataProvider;

import com.codecool.sqlyourcsv.model.Table;
import org.springframework.stereotype.Service;

@Service
public interface IDataProvider {
    Table query(String query) throws Exception;
}
