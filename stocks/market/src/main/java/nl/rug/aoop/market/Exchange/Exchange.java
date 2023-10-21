package nl.rug.aoop.market.Exchange;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.market.Transaction.Bid;
import nl.rug.aoop.market.Transaction.Transaction;

import java.util.*;


/**
 * Exchange is the class that manages the execution of orders.
 */
@Slf4j
@Getter
public class Exchange {
    private final List<Stock> stocks;
    private List<Trader> traders = new ArrayList<>();
    private final Map<Stock, SortedSet<Bid>> bids = new HashMap<>();
    private final Map<Stock, SortedSet<Ask>> asks = new HashMap<>();

    /**
     * Constructor for the Exchange with only stocks.
     * @param stocks Map of the stocks that should be registered in the exchange.
     */
    public Exchange(List<Stock> stocks) {
        this.stocks = stocks;
        for (Stock stock : this.stocks) {
            bids.put(stock,new TreeSet<Bid>());
            asks.put(stock,new TreeSet<Ask>());
        }
    }

    /**
     * Constructor for the exchange with the stocks and the traders.
     * @param stocks The stocks that should be registered in the exchange.
     * @param traders The traders that are acting on the exchange.
     */
    public Exchange(List<Stock> stocks, List<Trader> traders) {
        this(stocks);
        this.traders = traders;
    }

    /**
     * Adds a trader into the active traders on the exchange.
     * @param trader The trader that is on the exchange.
     */
    public void addTrader(Trader trader) {
        traders.add(trader);
    }

    /**
     * Adds a stock onto the exchange.
     * @param stock The stock to be added.
     */
    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    /**
     * Places a bid.
     * @param bid The bid that needs to be place.
     */
    public void placeBid(Bid bid) {
        SortedSet<Ask> relevantAsks = asks.get(bid.getStock());
        if (!relevantAsks.isEmpty()) {
            Ask highestAsk = relevantAsks.first();
            if (highestAsk.getPrice() >= bid.getPrice()) {
                resolveTrade(highestAsk, bid);
            }
        }
        bids.get(bid.getStock()).add(bid);
    }

    /**
     * Places an ask.
     * @param ask The ask to be placed.
     */
    public void placeAsk(Ask ask) {
        SortedSet<Bid> relevantBids = bids.get(ask.getStock());
        log.info(Boolean.toString(relevantBids.isEmpty()));
        if (!relevantBids.isEmpty()) {
            Bid lowestBid = relevantBids.last();
            if (lowestBid.getPrice() <= ask.getPrice()) {
                resolveTrade(ask, lowestBid);
            }
        }
        asks.get(ask.getStock()).add(ask);
    }

    /**
     * Executes a trade.
     * @param ask The ask to be traded.
     * @param bid The bid to be traded.
     */
    private void resolveTrade(Ask ask, Bid bid){
        if (ask.getShares() == bid.getShares()) {
            resolveBid(bid, ask.getPrice());
            resolveAsk(ask);
        } else if (ask.getShares() < bid.getShares()) {
            resolveAsk(ask);
            bid.setShares(bid.getShares() - ask.getShares());
            placeBid(bid);
        } else {
            resolveBid(bid,ask.getPrice());
            ask.setShares(ask.getShares()-bid.getShares());
            placeAsk(ask);
        }
    }

    /**
     * Resolves a bid when it has been fully traded and creates a corresponding transaction.
     * @param bid the bid to resolve.
     * @param price The price at which it was sold.
     */
    private void resolveBid(Bid bid, int price){ // TODO update stock price
        // TODO check if trader has funds
        bids.get(bid.getStock()).remove(bid);
        Transaction transaction = new Transaction(bid.getStock(),price,bid.getShares());
        bid.getTrader().getTransactionHistory().add(transaction);
        bid.getTrader().addFunds(price*bid.getShares());
        bid.getTrader().removeShares(bid.getStock(),bid.getShares());
    }

    /**
     * Resolves an ask when it has been fully traded and creates a corresponding transaction.
     * @param ask the bid to resolve.
     */
    private void resolveAsk(Ask ask){ // TODO update stock price
        // TODO check if trader has funds
        asks.get(ask.getStock()).remove(ask);
        Transaction transaction = new Transaction(ask.getStock(),ask.getPrice(),ask.getShares());
        ask.getTrader().getTransactionHistory().add(transaction);
        ask.getTrader().removeFunds(ask.getPrice()*ask.getShares());
        ask.getTrader().addShares(ask.getStock(),ask.getShares());
    }
}
