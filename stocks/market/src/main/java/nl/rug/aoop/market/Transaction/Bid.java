package nl.rug.aoop.market.Transaction;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;

import java.io.*;

/**
 * The class representing a bid that can be completed on the exchange.
 */
@Getter
public class Bid implements Comparable<Bid>, Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private final Trader trader;
    private final Stock stock;
    private final int price;
    @Setter private int shares;

    /**
     * Constructor for a bid.
     * @param trader Trader creating the bid
     * @param stock Stock to be traded
     * @param shares Amount of shares to bid
     * @param price Price at which to bid
     */
    public Bid(Trader trader, Stock stock, int shares, int price) {
        this.trader = trader;
        this.stock = stock;
        this.shares = shares;
        this.price = price;
    }

    @Override
    public int compareTo(Bid o) {
        return this.price-o.price;
    }
}