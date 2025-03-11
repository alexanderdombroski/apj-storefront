package edu.byui.apj.storefront.api.service;

import edu.byui.apj.storefront.api.model.TradingCard;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradingCardService {
    private final ArrayList<TradingCard> tradingCardList = new ArrayList<>();

    public TradingCardService() {
        loadTradingCardsFromCSV();
    }

    private void loadTradingCardsFromCSV() {
        try {
            ClassPathResource resource = new ClassPathResource("pioneers.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader);

            for (CSVRecord record : records) {
                // ID,Name,Specialty,Contribution,Price,ImageUrl
                tradingCardList.add(new TradingCard(
                        Long.parseLong(record.get("ID")),
                        record.get("Name"),
                        record.get("Specialty"),
                        record.get("Contribution"),
                        BigDecimal.valueOf(Double.parseDouble(record.get("Price"))),
                        record.get("ImageUrl")
                ));
            }
        } catch (IOException e) {
            System.err.println("Error loading pioneers.csv: " + e.getMessage()); // Log the message
            e.printStackTrace();
            throw new RuntimeException("Failed to load pioneers.csv", e);
        }
    }

    public List<TradingCard> sortAndFilter(String sortBy, BigDecimal minPrice, BigDecimal maxPrice, String specialty) {
        List<TradingCard> filteredList = new ArrayList<>(tradingCardList);

        // Filter by Price
        if (minPrice != null) {
            filteredList = filteredList.stream()
                    .filter(card -> card.getPrice().compareTo(minPrice) >= 0)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            filteredList = filteredList.stream()
                    .filter(card -> card.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        }

        // Filter by Specialty
        if (specialty != null && !specialty.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(card -> card.getSpecialty().equalsIgnoreCase(specialty))
                    .collect(Collectors.toList());
        }

        // Sort
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equalsIgnoreCase("Name")) {
                filteredList.sort(Comparator.comparing(TradingCard::getName));
            } else if (sortBy.equalsIgnoreCase("Price")) {
                filteredList.sort(Comparator.comparing(TradingCard::getPrice));
            }
        }

        return filteredList;
    }

    public List<TradingCard> search(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>(tradingCardList); // Return all if no query.
        }

        String lowerCaseQuery = query.toLowerCase();
        return tradingCardList.stream()
                .filter(card -> card.getName().toLowerCase().contains(lowerCaseQuery) ||
                        card.getContribution().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }


    public List<TradingCard> getPage(int page, int size) {
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, tradingCardList.size());

        if (startIndex >= tradingCardList.size()) {
            return new ArrayList<>(); // Return empty list if page is out of bounds.
        }

        return tradingCardList.subList(startIndex, endIndex);
    }
}
