package com.abdrakhimov.demo_corona.services;

import com.abdrakhimov.demo_corona.database.CountryRepository;
import com.abdrakhimov.demo_corona.models.Country;
import com.abdrakhimov.demo_corona.models.State;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class CoronaVirusServices {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String COUNTRY_HEADER = "Country/Region";
    private static final String STATE_HEADER = "Province/State";


    private final CountryRepository repository;

    @Getter
    private List<Country> allStats;


    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<Country> actualCountries = getActualCountriesStats();
        updateDatabaseData(actualCountries);
        this.allStats = actualCountries;
    }

    @Transactional
    protected void updateDatabaseData(List<Country> countries) {
        repository.deleteAll();
        repository.saveAll(countries);
    }

    private List<Country> getActualCountriesStats() throws IOException, InterruptedException {
        return requestVirusData()
                .stream()
                .collect(Collectors.groupingBy(ParsedRecord::getCountry))
                .entrySet()
                .stream()
                .map(entry -> new GrouppedRecords(entry.getKey(), entry.getValue()))
                .map(this::buildCountry)
                .collect(Collectors.toList());
    }

    private Country buildCountry(GrouppedRecords countryRecords) {
        List<ParsedRecord> records = countryRecords.getRecords();
        Country country = getCountryFromRecords(countryRecords);
        List<State> states = getCountryStatesFromRecords(records);
        states.forEach(state -> state.setCountry(country));
        country.setStates(states);
        return country;
    }

    private List<ParsedRecord> requestVirusData() throws IOException, InterruptedException {
        StringReader csvBodyReader = requestActualData();

        CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvBodyReader);

        TodayAndYesterdayHeaders todayAndYesterday = getTodayAndYesterday(parser.getHeaderNames());

        return parser.getRecords()
                .stream()
                .map(record -> parseCsvRecord(record, todayAndYesterday.getToday(), todayAndYesterday.getYesterday()))
                .collect(Collectors.toList());
    }

    private ParsedRecord parseCsvRecord(CSVRecord record, String todayHeader, String yesterdayHeader) {
        return new ParsedRecord(
                record.get(COUNTRY_HEADER),
                record.get(STATE_HEADER),
                Integer.parseInt(record.get(todayHeader)),
                Integer.parseInt(record.get(yesterdayHeader))
        );
    }

    private static StringReader requestActualData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new StringReader(httpResponse.body());
    }


    private static TodayAndYesterdayHeaders getTodayAndYesterday(List<String> headers) {
        int todayHeaderPosition = headers.size() - 1;
        int yesterdayHeaderPosition = headers.size() - 2;

        String todayHeader = headers.get(todayHeaderPosition);
        String yesterdayHeader = headers.get(yesterdayHeaderPosition);

        return new TodayAndYesterdayHeaders(todayHeader, yesterdayHeader);
    }

    private List<State> getCountryStatesFromRecords(List<ParsedRecord> records) {
        return records
                .stream()
                .filter(record -> !record.getState().isEmpty())
                .collect(Collectors.groupingBy(ParsedRecord::getState))
                .entrySet()
                .stream()
                .map(entry -> new GrouppedRecords(entry.getKey(), entry.getValue()))
                .map(CoronaVirusServices::buildStateWithoutCountry)
                .collect(Collectors.toList());
    }

    private static State buildStateWithoutCountry(GrouppedRecords data) {

        int todayStateStats = data.records
                .stream()
                .mapToInt(ParsedRecord::getStatsToday)
                .sum();

        int yesterdayStateStats = data.records
                .stream()
                .mapToInt(ParsedRecord::getStatsToday)
                .sum();

        return new State(data.getName(), null, todayStateStats, yesterdayStateStats);
    }

    private Country getCountryFromRecords(GrouppedRecords countryRecords) {
        List<ParsedRecord> countryRecordsWithoutState =
                countryRecords.getRecords()
                        .stream()
                        .filter(record -> record.getState().isEmpty())
                        .collect(Collectors.toList());

        int countryCurrentStats = countryRecordsWithoutState.stream()
                .mapToInt(record -> record.statsToday)
                .sum();

        int countryYesterdayStats = countryRecordsWithoutState.stream()
                .mapToInt(record -> record.statsYesterday)
                .sum();


        return new Country(
                null,
                countryRecords.getName(),
                countryCurrentStats,
                countryYesterdayStats
        );
    }

    private static int calculateStatsForRecordsAtDate(List<CSVRecord> records, String date) {
        return records.stream()
                .map(csvRecord -> csvRecord.get(date))
                .mapToInt(Integer::parseInt)
                .sum();
    }


    @Data
    private static class TodayAndYesterdayHeaders {
        private final String today;
        private final String yesterday;
    }


    @Data
    private static class GrouppedRecords {
        private final String name;
        private final List<ParsedRecord> records;
    }

    @Data
    private static class ParsedRecord {
        private final String country;
        private final String state;
        private final int statsToday;
        private final int statsYesterday;
    }

}
