package com.codecool.sqlyourcsv.controller;

import com.codecool.sqlyourcsv.dataProvider.IDataProvider;
import com.codecool.sqlyourcsv.dataProvider.QueryExecutor;
import com.codecool.sqlyourcsv.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String doGet(Model model) throws Exception {
        Table table = this.iDataProvider.query("Sheet1");
        model.addAttribute("table", table);
        return "index";
    }

    @PostMapping
    public String doPost(@RequestParam String query, Model model) throws Exception {
        Table table = this.iDataProvider.query("Sheet1");
        QueryExecutor executor = new QueryExecutor(table, query);
        model.addAttribute("table", executor.execute());
        return "index";
    }
}

