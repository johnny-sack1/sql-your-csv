package com.codecool.sqlyourcsv.controller;

import com.codecool.sqlyourcsv.dataProvider.IDataProvider;
import com.codecool.sqlyourcsv.dataProvider.QueryExecutor;
import com.codecool.sqlyourcsv.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private IDataProvider iDataProvider;
    private Table table;

    @Value("${git.commit.id}")
    private String commitId;

    @Autowired
    public WebController(IDataProvider iDataProvider) throws Exception {
        this.iDataProvider = iDataProvider;
        this.table = this.iDataProvider.query("Sheet1");
    }

    @GetMapping("/")
    public String doGet(Model model) throws Exception {
        model.addAttribute("commitId", this.commitId);
        model.addAttribute("table", this.table);
        return "index";
    }

    @PostMapping
    public String doPost(RedirectAttributes redirectAttrs, @RequestParam String query,
        Model model) {
        QueryExecutor executor = new QueryExecutor(this.table, query);
        try {
            table = executor.execute();
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("flashError", e);
            return "redirect:/";
        }
        model.addAttribute("commitId", this.commitId);
        model.addAttribute("table", this.table);
        return "index";
    }

    @PostMapping("/refresh")
    public String refresh(RedirectAttributes redirectAttrs) {
        try {
            this.table = this.iDataProvider.query("Sheet1");
            redirectAttrs.addFlashAttribute("fleshMessage",
                "Google Sheet Data Updated Successfully");
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }
}

