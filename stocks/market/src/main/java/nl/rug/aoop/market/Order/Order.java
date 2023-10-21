package nl.rug.aoop.market.Order;

import lombok.Getter;

/**
 * Abstract class depicting the features of an order.
 */
@Getter
public abstract class Order {
    private final String traderID;
    private final String stockSymbol;
    private final int stockAmount;

    /**
     * The type of order that it is, ENUM.
     */
    protected OrderType orderType;

    /**
     * Super constructor for an order.
     * @param traderID The trader that is generating the order.
     * @param stockSymbol The stock for which the order is placed.
     * @param stockAmount The amount of stocks for which the order is made.
     */
    public Order(String traderID, String stockSymbol, int stockAmount) {
        this.traderID = traderID;
        this.stockSymbol = stockSymbol;
        this.stockAmount = stockAmount;
    }
}
