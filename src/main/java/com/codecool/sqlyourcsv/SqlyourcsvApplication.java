package com.codecool.sqlyourcsv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SqlyourcsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlyourcsvApplication.class, args);
		//DataProvider provider = new DataProvider();
		//System.out.println(provider.query("SELECT * FROM 50contacts.csv;"));
	}
}
