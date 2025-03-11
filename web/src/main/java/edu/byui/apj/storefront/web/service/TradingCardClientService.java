package edu.byui.apj.storefront.web.service;

import edu.byui.apj.storefront.web.model.TradingCard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TradingCardClientService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public TradingCardClientService(RestTemplate restTemplate, @Value("${api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // Returns the list of cards starting at position page * size and returning size elements.
    public List<TradingCard> getAllCardsPaginated(int page, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("page", page)
                .queryParam("size", size);

        ResponseEntity<List<TradingCard>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TradingCard>>() {}
        );
        return response.getBody();
    }

    // Returns the list resulting in filtering by minPrice, maxPrice or specialty, then sorting by sort
    // Sort can be "name" or "price"
    public List<TradingCard> filterAndSort(BigDecimal minPrice, BigDecimal maxPrice, String specialty, String sort) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/filter");

        if (minPrice != null) builder.queryParam("minPrice", minPrice);
        if (maxPrice != null) builder.queryParam("maxPrice", maxPrice);
        if (specialty != null) builder.queryParam("specialty", specialty);
        if (sort != null) builder.queryParam("sort", sort);

        ResponseEntity<List<TradingCard>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TradingCard>>() {}
        );
        return response.getBody();
    }

    // Returns the list of cards resulting in the query string (case insensitive) {
    // found in the name or contribution.
    public List<TradingCard> searchByNameOrContribution(String query) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search")
                .queryParam("query", query);

        ResponseEntity<List<TradingCard>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TradingCard>>() {}
        );
        return response.getBody();
    }
}
