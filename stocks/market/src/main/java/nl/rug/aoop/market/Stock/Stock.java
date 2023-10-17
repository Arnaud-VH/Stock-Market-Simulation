package nl.rug.aoop.market.Stock;

import lombok.Getter;
import lombok.Setter;

/**
 * The class representing a Stock that can be bought and sold by traders.
 */
public class Stock {
    @Getter private final String symbol;
    @Getter @Setter private int price;
    @Getter private final String name;
    @Getter private int outstandingShares;
    @Getter private int marketCap;

    /**
     * The constructor for a Stock.
     * @param symbol The symbol of the stock.
     * @param price The price of the stock.
     * @param name The name of the stock.
     * @param outstandingShares The amount of shares on the market of the stock.
     */
    public Stock(String symbol, int price, String name, int outstandingShares) {
        this.symbol = symbol;
        this.price = price;
        this.name = name;
        this.outstandingShares = outstandingShares;
        this.marketCap = price*outstandingShares;
    }
}
