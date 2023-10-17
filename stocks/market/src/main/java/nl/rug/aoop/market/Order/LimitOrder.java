package nl.rug.aoop.market.Order;

/**
 * Type of order which buys or sells at a given price.
 */
public class LimitOrder extends Order{
    private int price;

    /**
     * Constructor for the Limit order.
     * @param traderID The trader that is generating this order.
     * @param stockSymbol The stock for which the order is placed.
     * @param amount The amount of stocks the order is for.
     * @param price The limit price.
     */
    public LimitOrder(String traderID, String stockSymbol, int amount, int price) {
        super(traderID, stockSymbol, amount);
        this.price = price;
        this.orderType = OrderType.LIMIT_ORDER;
    }
}
