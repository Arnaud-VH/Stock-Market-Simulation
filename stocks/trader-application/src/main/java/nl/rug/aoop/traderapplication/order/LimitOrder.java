package nl.rug.aoop.traderapplication.order;

import lombok.Getter;
import nl.rug.aoop.market.stock.Stock;

/**
 * Type of order which buys or sells at a given price.
 */
@Getter
public class LimitOrder extends Order{
    private final double price;

    /**
     * Constructor for the Limit order.
     * @param traderID The trader that is generating this order.
     * @param stock The stock for which the order is placed.
     * @param amount The amount of stocks the order is for.
     * @param price The limit price.
     */
    public LimitOrder(String traderID, Stock stock, long amount, double price) {
        super(traderID, stock, amount);
        this.price = price;
        this.orderType = OrderType.LIMIT_ORDER;
    }
}
