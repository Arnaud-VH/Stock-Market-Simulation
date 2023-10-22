package nl.rug.aoop.market.Transaction;

import lombok.Getter;
import nl.rug.aoop.market.Stock.Stock;

/**
 * Transaction represented as a record.
 * @param amount The amount of stocks the were traded.
 * @param stockPrice The price at which the stock was traded.
 * @param stock The stock that was traded.
 */
public record Transaction(@Getter Stock stock, @Getter int stockPrice, @Getter int amount){
    /**
     * Custom constructor for the Transaction that updates the stock price accordingly.
     * @param stock The stock that was traded.
     * @param stockPrice The price at which the stock was traded.
     * @param amount The amount of shares of the stock that were traded.
     */
    public Transaction {
        stock.setPrice(stockPrice);
    }

}
