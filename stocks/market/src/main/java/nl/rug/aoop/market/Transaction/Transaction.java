package nl.rug.aoop.market.Transaction;

import lombok.Getter;

/**
 * The class that keeps track of which stocks have been traded at what price and how many.
 */
public class Transaction { //May be made into a record
    @Getter private final String stockSymbol;
    @Getter private final int stockPrice;
    @Getter private final int amount;

    /**
     * The constructor for a transaction.
     * @param stockSymbol The symbol of the stock that was traded.
     * @param stockPrice The price at which the stock was traded.
     * @param amount The amount of stocks that were traded.
     */
    public Transaction(String stockSymbol, int stockPrice, int amount) {
        this.stockSymbol = stockSymbol;
        this.stockPrice = stockPrice;
        this.amount = amount;
    }
}
