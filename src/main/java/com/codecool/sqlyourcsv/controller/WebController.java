package com.codecool.sqlyourcsv.controller;

import com.codecool.sqlyourcsv.dataProvider.IDataProvider;
import com.codecool.sqlyourcsv.dataProvider.QueryExecutor;
import com.codecool.sqlyourcsv.model.Table;
import com.codecool.sqlyourcsv.queryParser.InvalidQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String doPost(RedirectAttributes redirectAttrs, @RequestParam String query, Model model)
        throws Exception {
        Table table = this.iDataProvider.query("Sheet1");
        QueryExecutor executor = new QueryExecutor(table, query);
        try {
            table = executor.execute();
        } catch (InvalidQueryException e) {
            redirectAttrs.addFlashAttribute("flashError", e);
            return "redirect:/";
        }
        model.addAttribute("table", table);
        return "index";
    }
}

