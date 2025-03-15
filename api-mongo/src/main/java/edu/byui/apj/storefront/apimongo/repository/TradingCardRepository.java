package edu.byui.apj.storefront.apimongo.repository;


import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import edu.byui.apj.storefront.apimongo.model.TradingCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingCardRepository extends MongoRepository<TradingCard, String> {

    // âœ… Returns all trading cards with sorting and paging
    @NonNull
    Page<TradingCard> findAll(@NonNull Pageable pageable);

    // âœ… Filters by specialty
    List<TradingCard> findBySpecialty(String specialty);

    // âœ… Filters by price range
    List<TradingCard> findByPriceBetween(Double minPrice, Double maxPrice);

    // âœ… Searches for text within name or contribution
    List<TradingCard> findByNameContainsIgnoreCaseOrContributionContainsIgnoreCase(String name, String contribution);
}