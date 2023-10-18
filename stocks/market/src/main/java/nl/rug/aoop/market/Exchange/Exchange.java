package nl.rug.aoop.market.Exchange;

import lombok.Getter;
import nl.rug.aoop.market.Order.Order;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Transaction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Exchange is the class that manages the execution of orders.
 */
@Getter
public class Exchange {
    private final Map<String, Stock> stocks;
    private Map<String, Trader> traders = new HashMap<>();
    private List<Transaction> transactions = new LinkedList<>();
    // maybe make transactions implement comparable and keep in sorted map? depends on use of transactions
    private List<Order> bids = new LinkedList<>();
    private List<Order> asks = new LinkedList<>();

    /**
     * Constructor for the Exchange with only stocks.
     * @param stocks Map of the stocks that should be registered in the exchange.
     */
    public Exchange(Map<String, Stock> stocks) {
        this.stocks = stocks;
    }

    /**
     * Constructor for the exchange with the stocks and the traders.
     * @param stocks The stocks that should be registered in the exchange.
     * @param traders The traders that are acting on the exchange.
     */
    public Exchange(Map<String, Stock> stocks, Map<String, Trader> traders) {
        this(stocks);
        this.traders = traders;
    }

    /**
     * Adds a trader into the active traders on the exchange.
     * @param trader The trader that is on the exchange.
     */
    public void addTrader(Trader trader) {
        traders.put(trader.getId(),trader);
    }

    /**
     * Adds a stock onto the exchange.
     * @param stock The stock to be added.
     */
    public void addStock(Stock stock) {
        stocks.put(stock.getSymbol(),stock);
    }

    /**
     * Executes the buy order.
     * @param order The order, of specific type, that needs to be executed.
     */
    public void executeBuyOrder(Order order) {
        // TODO try to resolve buy order with command pattern
        asks.add(order); // if it cannot be resolved add to asks
    }

    /**
     * Executes the sell order.
     * @param order The order, of specific type, that needs to be executed.
     */
    public void executeSellOrder(Order order) {
        // TODO try to resolve sell order with command pattern
        bids.add(order); // if it cannot be resolved add to bids
    }
}
