package nl.rug.aoop.traderapplication.order;

import nl.rug.aoop.market.stock.Stock;

/**
 * Type of order that buys or sells at the current market price.
 */
public class MarketOrder extends Order{

    /**
     * Constructor for the market order.
     * @param traderID The trader that is generating the order.
     * @param stock The stock on which the order is placed.
     * @param amount The amount of stocks the order is for.
     */
    public MarketOrder(String traderID, Stock stock, int amount) {
        super(traderID, stock, amount);
        this.orderType = OrderType.MARKET_ORDER;
    }
}
