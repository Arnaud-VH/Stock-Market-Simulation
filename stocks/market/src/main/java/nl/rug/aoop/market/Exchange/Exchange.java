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

@Getter
public class Exchange {
    private final Map<String, Stock> stocks;
    private Map<String, Trader> traders = new HashMap<>();
    private List<Transaction> transactions = new LinkedList<>(); // maybe make transactions implement comparable and keep in sorted map? depends on use of transactions
    private List<Order> bids = new LinkedList<>();
    private List<Order> asks = new LinkedList<>();

    public Exchange(Map<String, Stock> stocks) {
        this.stocks = stocks;
    }
    public Exchange(Map<String, Stock> stocks, Map<String, Trader> traders) {
        this(stocks);
        this.traders = traders;
    }
    public void addTrader(Trader trader) {
        traders.put(trader.getId(),trader);
    }
    public void addStock(Stock stock) {
        stocks.put(stock.getSymbol(),stock);
    }
    public void executeBuyOrder(Order order) {
        // TODO try to resolve buy order with command pattern
        asks.add(order); // if it cannot be resolved add to asks
    }
    public void executeSellOrder(Order order) {
        // TODO try to resolve sell order with command pattern
        bids.add(order); // if it cannot be resolved add to bids
    }
}
