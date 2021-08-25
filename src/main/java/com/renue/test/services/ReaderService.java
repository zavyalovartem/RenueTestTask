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
import java.util.TreeMap;

@Service
@EnableConfigurationProperties
@ConfigurationProperties("settings")
public class ReaderService {

    @Value("${settings.default-search-column}")
    private Integer searchColumn;

    public Integer externalSearchColumn;

    public void readAll(java.io.Reader reader, String searchString) throws IOException, CsvException {
        CSVReader csvReader = new CSVReader(reader);
        var sortingMap = new TreeMap<String, ArrayList<String[]>>();
        String[] row;
        long startTime = System.nanoTime();
        while ((row = csvReader.readNext()) != null){
            var sortColumn = row[searchColumn - 1];
            if (sortColumn.startsWith(searchString)) {
                if (sortingMap.containsKey(sortColumn)){
                    sortingMap.get(sortColumn).add(row);
                } else{
                    var list =  new ArrayList<String[]>();
                    list.add(row);
                    sortingMap.put(sortColumn, list);
                }
            }
        }
        long endTime = System.nanoTime();
        reader.close();
        csvReader.close();
        var amount = sortingMap.size();
        for (var entry : sortingMap.entrySet()){
            for (var val : entry.getValue()) {
                System.out.println(String.join(" ", val));
            }
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
        if (externalSearchColumn != null){
            searchColumn = externalSearchColumn;
        }
        readAll(reader, str);
    }
}
