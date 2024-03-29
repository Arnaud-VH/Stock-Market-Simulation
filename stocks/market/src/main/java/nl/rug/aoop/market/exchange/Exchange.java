package nl.rug.aoop.market.exchange;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.market.transaction.Ask;
import nl.rug.aoop.market.transaction.Bid;
import nl.rug.aoop.market.transaction.Transaction;

import java.util.*;


/**
 * Exchange is the class that represents the exchange.
 */
@Slf4j
@Getter
public class Exchange {
    private final ArrayList<Stock> stocks;
    private ArrayList<Trader> traders;
    private final Map<Stock, SortedSet<Bid>> bids = new HashMap<>();
    private final Map<Stock, SortedSet<Ask>> asks = new HashMap<>();

    /**
     * Constructor for the Exchange with only stocks.
     * @param stocks Map of the stocks that should be registered in the exchange.
     */
    public Exchange(ArrayList<Stock> stocks) {
        this.stocks = stocks;
        for (Stock stock : this.stocks) {
            log.info("Creating bids and asks for stock " + stock.getSymbol());
            bids.put(stock,new TreeSet<>());
            asks.put(stock,new TreeSet<>());
        }
    }

    /**
     * Constructor for the exchange with the stocks and the traders.
     * @param stocks The stocks that should be registered in the exchange.
     * @param traders The traders that are acting on the exchange.
     */
    public Exchange(ArrayList<Stock> stocks, ArrayList<Trader> traders) {
        this(stocks);
        this.traders = traders;
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
        getAsks(ask.getStock()).add(ask);
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

    private void resolveBid(Bid bid, double price){
        getBids(bid.getStock()).remove(bid);
        Transaction transaction = new Transaction(bid.getStock(),price,bid.getShares());
        bid.getTrader().getTransactionHistory().add(transaction);
        bid.getTrader().addFunds(price*bid.getShares());
        bid.getTrader().removeShares(bid.getStock(),bid.getShares());
        bid.getStock().setPrice(price);
        int index = getStockIndex(bid.getStock());
        stocks.get(index).setPrice(bid.getPrice());
        log.info("Trader " + bid.getTrader().getId() + " sold " + bid.getShares()
            + " shares of " + bid.getStock().getSymbol() + " for a total of " +
                price * bid.getShares() + "$ currency");
    }

    /**
     * Resolves an ask when it has been fully traded and creates a corresponding transaction.
     * @param ask the bid to resolve.
     */
    private void resolveAsk(Ask ask){
        getAsks(ask.getStock()).remove(ask);
        Transaction transaction = new Transaction(ask.getStock(),ask.getPrice(),ask.getShares());
        ask.getTrader().getTransactionHistory().add(transaction);
        ask.getTrader().removeFunds(ask.getPrice()*ask.getShares());
        ask.getTrader().addShares(ask.getStock(),ask.getShares());
        int index = getStockIndex(ask.getStock());
        stocks.get(index).setPrice(ask.getPrice());
        ask.getStock().setPrice(ask.getPrice());
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
    private void resolvePartialBid(Bid bid, long shares, double price) {
        Transaction transaction = new Transaction(bid.getStock(), price, shares);
        bid.getTrader().getTransactionHistory().add(transaction);
        bid.getTrader().addFunds(shares*price);
        int indexTrader = getTraderIndex(bid.getTrader());
        traders.get(indexTrader).removeFunds(shares*bid.getPrice());
        traders.get(indexTrader).addShares(bid.getStock(),shares);
        bid.getTrader().removeShares(bid.getStock(),shares);
        bid.setShares(bid.getShares()-shares);
        bid.getStock().setPrice(price);
        int index = getStockIndex(bid.getStock());
        stocks.get(index).setPrice(bid.getPrice());
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
    private void resolvePartialAsk(Ask ask, long shares) {
        Transaction transaction = new Transaction(ask.getStock(), ask.getPrice(), shares);
        ask.getTrader().getTransactionHistory().add(transaction);
        int indexTrader = getTraderIndex(ask.getTrader());
        traders.get(indexTrader).removeFunds(shares* ask.getPrice());
        traders.get(indexTrader).addShares(ask.getStock(),shares);
        ask.getTrader().removeFunds(shares*ask.getPrice());
        ask.getTrader().addShares(ask.getStock(),shares);
        ask.setShares(ask.getShares()-shares);
        int indexStock = getStockIndex(ask.getStock());
        stocks.get(indexStock).setPrice(ask.getPrice());
        ask.getStock().setPrice(ask.getPrice());
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

    private int getStockIndex(Stock stock) {
        int index = 0;
        for (Stock s : stocks) {
            if (s.equals(stock)) {
                break;
            } else {
                index++;
            }
        }
        return index;
    }

    private int getTraderIndex(Trader trader) {
        int index = 0;
        for (Trader t : traders) {
            if (t.equals(trader)) {
                break;
            } else {
                index++;
            }
        }
        return index;
    }
}
