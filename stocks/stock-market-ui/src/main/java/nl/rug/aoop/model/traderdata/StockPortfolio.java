package nl.rug.aoop.model.traderdata;

import lombok.Data;
import java.util.Map;

/**
 * Used to load in owned shares from the Yaml.
 */
@Data
public class StockPortfolio {
    private Map<String, Long> ownedShares;

    /**
     * Default constructor for the YAML.
     */
    public StockPortfolio() {}

    /**
     * Constructor for the Stock Portfolio.
     * @param ownedShares The shares that the.
     */
    public StockPortfolio(Map<String, Long> ownedShares) {
        this.ownedShares = ownedShares;
    }
}
