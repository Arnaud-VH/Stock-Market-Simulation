package nl.rug.aoop.market.Transaction;

import lombok.Getter;

/**
 * Transaction represented as a record.
 * @param amount The amount of stocks the were traded.
 * @param stockPrice The price at which the stock was traded.
 * @param stockSymbol The symbol of the stock that was traded.
 */
public record Transaction(@Getter String stockSymbol, @Getter int stockPrice, @Getter int amount){}