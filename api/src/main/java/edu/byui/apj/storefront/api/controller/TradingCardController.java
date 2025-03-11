package edu.byui.apj.storefront.api.controller;

import edu.byui.apj.storefront.api.model.TradingCard;
import edu.byui.apj.storefront.api.service.TradingCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class TradingCardController {

    private final TradingCardService tradingCardService;

    @Autowired
    public TradingCardController(TradingCardService tradingCardService) {
        this.tradingCardService = tradingCardService;
    }

    @GetMapping("/api/cards")
    public List<TradingCard> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return tradingCardService.getPage(page, size);
    }

    @GetMapping("/api/cards/filter")
    public List<TradingCard> filterTradingCards(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String sort) {
        return tradingCardService.sortAndFilter(sort, minPrice, maxPrice, specialty);
    }

    @GetMapping("/api/cards/search")
    public List<TradingCard> searchTradingCards(@RequestParam() String query) {
        return tradingCardService.search(query);
    }

}
