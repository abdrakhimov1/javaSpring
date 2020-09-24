package com.abdrakhimov.demo_corona.services;

import com.abdrakhimov.demo_corona.database.CountryRepository;
import com.abdrakhimov.demo_corona.models.Country;
import com.abdrakhimov.demo_corona.models.LocationStats;
import com.abdrakhimov.demo_corona.models.State;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CoronaVirusServices {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<Country> allStats;

    private String yesterday;
    private String today;


    @Autowired
    CountryRepository repository;

    public List<Country> getAllStats() {
        return allStats;
    }

    private String[] getTodayAndYesterday() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        List<String> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvBodyReader).getHeaderNames();
        return new String[] {records.get(records.size() - 1), records.get(records.size() - 2)};
    }

    private List<CSVRecord> requestVirusData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        List<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvBodyReader).getRecords();

        String[] todayAndYesterday = getTodayAndYesterday();
        this.today = todayAndYesterday[0];
        this.yesterday = todayAndYesterday[1];
        return records;
    }

    private State buildState(Map.Entry<String, List<CSVRecord>> k, Country country) {
        return new State(k.getKey(), country, k.getValue()
                .stream()
                .mapToInt(r -> Integer.valueOf(r.get(this.today)))
                .sum(), k.getValue()
                .stream()
                .mapToInt(r -> Integer.valueOf(r.get(this.yesterday)))
                .sum());
    }

    private List<State> recordsList(List<CSVRecord> records, Country country){
        return records
                .stream()
                .filter(o -> !o.get("Province/State").isEmpty())
                .collect(Collectors.groupingBy(record -> record.get("Province/State")))
                .entrySet()
                .stream()
                .map(k -> buildState(k, country)).collect(Collectors.toList());
    }

    private Country setCountryAll(String countryName, List<State> stateList, Country country, Map.Entry<String, List<CSVRecord>> countryRecords) {
        country.setCountryName(countryName);
        country.setStates(stateList);
        country.setStats(countryRecords.getValue()
                .stream()
                .mapToInt(r -> Integer.valueOf(r.get(this.today)))
                .sum());
        country.setPrevStats(countryRecords.getValue()
                .stream()
                .mapToInt(r -> Integer.valueOf(r.get(this.yesterday)))
                .sum());
        return country;
    }

    private List<Country> parseCSVData() throws IOException, InterruptedException {
        return requestVirusData()
                .stream()
                .collect(Collectors.groupingBy(record -> record.get("Country/Region")))
                .entrySet()
                .stream()
                .map(countryRecords -> {
                    Country country = new Country();
                    String countryName = countryRecords.getKey();
                    List<CSVRecord> records = countryRecords.getValue();
                    List<State> stateList = recordsList(records, country);
                    country = setCountryAll(countryName, stateList, country, countryRecords);
                    return country;
                })
                .collect(Collectors.toList());
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        if (this.allStats == null) {
            List<Country> countryList = parseCSVData();
            repository.deleteAll();
            repository.saveAll(countryList);
            this.allStats = countryList;
        } else {

        }
    }
}
