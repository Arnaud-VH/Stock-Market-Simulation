package nl.rug.aoop.market.Trader;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The class representing a trader that buys and sells stocks.
 */
public class Trader {
    @Getter private final int id;
    @Getter private final String name;
    @Getter @Setter private int funds;
    @Getter @Setter private Map<String,Integer> ownedStocks;

    /**
     * Constructor for the trader.
     * @param id The ID of the trader.
     * @param name The name of the trader.
     * @param funds The amount of funds the trader has.
     * @param ownedStocks The stocks that the trader owns By symbol and amount.
     */
    public Trader(int id, String name, int funds, Map<String, Integer> ownedStocks) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.ownedStocks = ownedStocks;
    }

}
