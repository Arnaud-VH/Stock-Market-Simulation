package nl.rug.aoop.market.Order;

/**
 * Type of order that buys or sells at the current market price.
 */
public class MarketOrder extends Order{

    /**
     * Constructor for the market order.
     * @param traderID The trader that is generating the order.
     * @param stockSymbol The stock on which the order is placed.
     * @param amount The amount of stocks the order is for.
     */
    public MarketOrder(String traderID, String stockSymbol,  int amount) {
        super(traderID, stockSymbol, amount);
        this.orderType = OrderType.MARKET_ORDER;
    }
}
