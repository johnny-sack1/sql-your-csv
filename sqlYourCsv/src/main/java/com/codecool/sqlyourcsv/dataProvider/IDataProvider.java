package com.codecool.sqlyourcsv.dataProvider;

import org.springframework.stereotype.Service;

@Service
public interface IDataProvider {
    String query(String query);
}
