package com.renue.test.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;

@Service
@EnableConfigurationProperties
@ConfigurationProperties("settings")
public class ReaderService {

    @Value("${settings.default-search-column}")
    private Integer searchColumn;

    public Integer externalSearchColumn;

    public void readAll(java.io.Reader reader, String searchString) throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(reader);
        var result = new ArrayList<String[]>();
        String[] row;
        long startTime = System.nanoTime();
        while ((row = csvReader.readNext()) != null){
            var col = row[searchColumn - 1];
            if (col.startsWith(searchString)) {
                result.add(row);
            }
        }
        result.stream().sorted(Comparator.comparing(o -> o[searchColumn - 1]));
        long endTime = System.nanoTime();
        reader.close();
        csvReader.close();
        var amount = result.size();
        for (var resRow : result){
            System.out.println(String.join(" ", resRow));
        }
        var resTime = (endTime - startTime) / 1000000;
        System.out.println("Количество найденных строк: " + amount);
        System.out.println("Время, затраченное на поиск: " + resTime + " мс");
    }

    public void testReturn() throws URISyntaxException, IOException, CsvException {
        java.io.Reader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/airports.csv")));
        BufferedReader consoleReader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Введите строку:");
        var str = consoleReader.readLine();
        consoleReader.close();
        if (str == null){
            throw new IllegalArgumentException("Введённая строка не может иметь значение null");
        }
        if (externalSearchColumn != null){
            searchColumn = externalSearchColumn;
        }
        readAll(reader, str);
    }
}
