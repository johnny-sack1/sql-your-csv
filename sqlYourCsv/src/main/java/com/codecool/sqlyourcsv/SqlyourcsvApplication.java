package com.codecool.sqlyourcsv;

import com.codecool.sqlyourcsv.dataProvider.DataProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SqlyourcsvApplication {

	public static void main(String[] args) {
		DataProvider provider = new DataProvider();
		System.out.println(provider.query("SELECT * FROM 50contacts.csv;"));
	}
}
