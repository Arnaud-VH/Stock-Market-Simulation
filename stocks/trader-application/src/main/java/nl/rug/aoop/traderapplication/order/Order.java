package nl.rug.aoop.traderapplication.order;

import lombok.Getter;
import nl.rug.aoop.market.stock.Stock;

/**
 * Abstract class depicting the features of an order.
 */
@Getter
public abstract class Order {
    private final String traderID;
    private final Stock stock;
    private final long stockAmount;

    /**
     * The type of order that it is, ENUM.
     */
    protected OrderType orderType;

    /**
     * Super constructor for an order.
     * @param traderID The trader that is generating the order.
     * @param stock The stock for which the order is placed.
     * @param stockAmount The amount of stocks for which the order is made.
     */
    public Order(String traderID, Stock stock, long stockAmount) {
        this.traderID = traderID;
        this.stock = stock;
        this.stockAmount = stockAmount;
    }
}
