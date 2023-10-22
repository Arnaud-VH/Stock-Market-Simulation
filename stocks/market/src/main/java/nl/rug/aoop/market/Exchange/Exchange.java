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
 * Exchange is the class that represents the exchange.
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
        log.info("Trader:" + bid.getTrader().getId() + " is placing a bid for " + bid.getShares()
                + " shares of:" + bid.getStock().getSymbol());
        if (!validBid(bid)) {
            log.info("Bid cancelled because trader " + bid.getTrader().getId() + " bid " +
                    bid.getShares() + " shares but only owns " + bid.getTrader().getShares(bid.getStock()));
            return;
        }
        SortedSet<Ask> relevantAsks = getAsks(bid.getStock());
        if (!relevantAsks.isEmpty()) {
            Ask highestAsk = relevantAsks.first();
            if (highestAsk.getPrice() >= bid.getPrice()) {
                resolveTrade(highestAsk, bid);
            }
        }
        getBids(bid.getStock()).add(bid);
    }

    /**
     * Places an ask.
     * @param ask The ask to be placed.
     */
    public void placeAsk(Ask ask) {
        log.info("Trader:" + ask.getTrader().getId() + " is placing an ask for " + ask.getShares()
                + " shares of:" + ask.getStock().getSymbol());
        if (!validAsk(ask)) {
            log.info("Ask not placed because trader " + ask.getTrader().getId() + " has insufficient funds");
            return;
        }
        SortedSet<Bid> relevantBids = getBids(ask.getStock());
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
        if (!validTrade(ask, bid)) {
            return;
        }
        if (ask.getShares() == bid.getShares()) {
            resolveBid(bid, ask.getPrice());
            resolveAsk(ask);
        } else if (ask.getShares() < bid.getShares()) {
            resolveAsk(ask);
            resolvePartialBid(bid,ask.getShares(),ask.getPrice());
        } else {
            resolveBid(bid,ask.getPrice());
            resolvePartialAsk(ask,bid.getShares());
        }
    }

    /**
     * Checks if the traders are eligible to trade wrt their portfolio.
     * @param ask The ask that is trying to be resolved.
     * @param bid The big that is trying to be resolved.
     * @return True if traders are valid, false if traders cannot trade.
     */
    private boolean validTrade(Ask ask, Bid bid) {
        //TODO test this with edge cases and see if it verifies
        boolean validBid = validBid(bid);
        boolean validAsk = validAsk(ask);
        if (!validBid) {
            getBids(bid.getStock()).remove(bid);
            log.info("Bid cancelled because trader " + bid.getTrader().getId() + " bid " +
                    bid.getShares() + " shares but only owns " + bid.getTrader().getShares(bid.getStock()));
        }
        if (!validAsk) {
            getAsks(ask.getStock()).remove(ask);
            log.info("Ask cancelled because trader " + ask.getTrader().getId() + " has insufficient funds");
        }
        return (validBid && validAsk);
    }

    private boolean validAsk(Ask ask) {
        return ask.getTrader().getFunds() >= (ask.getPrice()*ask.getShares());
    }

    private boolean validBid(Bid bid) {
        return bid.getTrader().getShares(bid.getStock()) >= bid.getShares();
    }

    /**
     * Resolves a bid when it has been fully traded and creates a corresponding transaction.
     * @param bid the bid to resolve.
     * @param price The price at which it was sold.
     */

    private void resolveBid(Bid bid, int price){
        // TODO update stock price
        getBids(bid.getStock()).remove(bid);
        Transaction transaction = new Transaction(bid.getStock(),price,bid.getShares());
        bid.getTrader().getTransactionHistory().add(transaction);
        bid.getTrader().addFunds(price*bid.getShares());
        bid.getTrader().removeShares(bid.getStock(),bid.getShares());
        log.info("Trader " + bid.getTrader().getId() + " sold " + bid.getShares()
            + " shares of " + bid.getStock().getSymbol() + " for a total of " +
                price * bid.getShares() + "$ currency");
    }

    /**
     * Resolves an ask when it has been fully traded and creates a corresponding transaction.
     * @param ask the bid to resolve.
     */
    private void resolveAsk(Ask ask){
        // TODO update stock price
        getAsks(ask.getStock()).remove(ask);
        Transaction transaction = new Transaction(ask.getStock(),ask.getPrice(),ask.getShares());
        ask.getTrader().getTransactionHistory().add(transaction);
        ask.getTrader().removeFunds(ask.getPrice()*ask.getShares());
        ask.getTrader().addShares(ask.getStock(),ask.getShares());
        log.info("Trader " + ask.getTrader().getId() + " bought " + ask.getShares()
                + " shares of " + ask.getStock().getSymbol() + " for a total of " +
                ask.getPrice() * ask.getShares() + "$ currency");
    }

    /**
     * Resolves and updates a bid that has partially been traded and creates a corresponding transaction.
     * @param bid Bid to partially resolve
     * @param shares Amount of shares to trade
     * @param price Price at which is being traded
     */
    private void resolvePartialBid(Bid bid, int shares, int price) {
        Transaction transaction = new Transaction(bid.getStock(), price, shares);
        bid.getTrader().getTransactionHistory().add(transaction);
        bid.getTrader().addFunds(shares*price);
        bid.getTrader().removeShares(bid.getStock(),shares);
        bid.setShares(bid.getShares()-shares);
        log.info("Trader " + bid.getTrader().getId() + " sold " + shares
                + " shares of " + bid.getStock().getSymbol() + " for a total of " +
                price * shares + "$ currency");
        placeBid(bid);
    }

    /**
     * Resolves and updates an ask that has partially been traded and creates a corresponding transaction.
     * @param ask Ask to partially resolve
     * @param shares Amount of shares to trade
     */
    private void resolvePartialAsk(Ask ask, int shares) {
        Transaction transaction = new Transaction(ask.getStock(), ask.getPrice(), shares);
        ask.getTrader().getTransactionHistory().add(transaction);
        ask.getTrader().removeFunds(shares*ask.getPrice());
        ask.getTrader().addShares(ask.getStock(),shares);
        ask.setShares(ask.getShares()-shares);
        log.info("Trader " + ask.getTrader().getId() + " bought " + shares
                + " shares of " + ask.getStock().getSymbol() + " for a total of " +
                ask.getPrice() * shares + "$ currency");
        placeAsk(ask);
    }

    /**
     * Gets bids of certain stock.
     * @param stock Stock to get bids for
     * @return Sorted set of bids
     */
    public SortedSet<Bid> getBids(Stock stock) {
        return bids.get(stock);
    }

    /**
     * Gets asks of certain stock.
     * @param stock Stock to get asks for
     * @return Sorted set of asks
     */
    public SortedSet<Ask> getAsks(Stock stock) {
        return asks.get(stock);
    }
}
