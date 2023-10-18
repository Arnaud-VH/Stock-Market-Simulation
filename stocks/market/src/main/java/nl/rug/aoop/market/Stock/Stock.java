package nl.rug.aoop.market.Stock;

import lombok.Getter;
import lombok.Setter;

/**
 * The class representing a Stock that can be bought and sold by traders.
 */
@Getter
public class Stock {
    private final String symbol;
    private int price;
    private final String name;
    private final int outstandingShares;
    private int marketCap;

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
    private void updateMarketCap() {
        this.marketCap = price*outstandingShares;
    }
    public void setPrice(int price) {
        this.price = price;
        updateMarketCap();
    }
}
