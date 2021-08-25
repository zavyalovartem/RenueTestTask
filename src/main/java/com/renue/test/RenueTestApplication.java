package com.renue.test;

import com.opencsv.exceptions.CsvException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.renue.test.services.ReaderService;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class RenueTestApplication implements CommandLineRunner {

	private final ReaderService parser;

	public RenueTestApplication(ReaderService readerService) { this.parser = readerService; }

	public static void main(String[] args){
		var app = new SpringApplication(RenueTestApplication.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws CsvException, IOException, URISyntaxException {
		Integer searchColumn = null;
		if (args.length != 0){
			searchColumn = Integer.parseInt(args[0]);
		}
		parser.externalSearchColumn = searchColumn;
		parser.testReturn();
	}

}
