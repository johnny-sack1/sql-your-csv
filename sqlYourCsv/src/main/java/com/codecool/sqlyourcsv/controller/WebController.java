package com.codecool.sqlyourcsv.controller;

import com.codecool.sqlyourcsv.dataProvider.IDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    private IDataProvider iDataProvider;

    @Autowired
    public WebController(IDataProvider iDataProvider) {
        this.iDataProvider = iDataProvider;
    }


    @GetMapping("/")
    public String doGet() {
        return "index";
    }

    @PostMapping("/")
    @ResponseBody
    public String doPost(@RequestParam("query") String query) {
        return this.iDataProvider.query(query);
        //return "index";
    }
}

