package com.codecool.sqlyourcsv.controller;

import com.codecool.sqlyourcsv.dataProvider.IDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WebController {

    private IDataProvider iDataProvider;

    @Autowired
    public WebController(IDataProvider iDataProvider) {
        this.iDataProvider = iDataProvider;
    }


    public String doGet() {
        return "index";
    }
}

