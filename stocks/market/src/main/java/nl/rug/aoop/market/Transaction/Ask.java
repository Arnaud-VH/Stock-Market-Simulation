package nl.rug.aoop.market.Transaction;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;

/**
 * The class representing an Ask that can be completed on an exchange.
 */
@Getter
public class Ask implements Comparable<Ask> {
    private final Trader trader;
    private final Stock stock;
    private final int price;
    @Setter private int shares;

    /**
     * Constructor for an ask.
     * @param trader Trader that is creating the ask.
     * @param stock Stock to be trader.
     * @param shares Amount of shares to purchase.
     * @param price Highest price at which to purchase.
     */
    public Ask(Trader trader, Stock stock, int shares, int price) {
        this.trader = trader;
        this.stock = stock;
        this.shares = shares;
        this.price = price;
    }

    @Override
    public int compareTo(Ask o) {
        return o.price-this.price;
    }
}
