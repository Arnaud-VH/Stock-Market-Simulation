package nl.rug.aoop.market.Transaction;

import lombok.Getter;
import nl.rug.aoop.market.Stock.Stock;

/**
 * Transaction represented as a record.
 * @param amount The amount of stocks the were traded.
 * @param stockPrice The price at which the stock was traded.
 * @param stock The stock that was traded.
 */
@Getter
public record Transaction(Stock stock, int stockPrice, int amount){
    public Transaction {
        stock.setPrice(stockPrice);
    }

}
