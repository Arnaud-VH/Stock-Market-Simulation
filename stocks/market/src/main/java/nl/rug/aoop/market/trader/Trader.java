package nl.rug.aoop.market.trader;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.transaction.Transaction;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * The class representing a trader that buys and sells stocks.
 */
@Getter
public class Trader implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    private final String id;
    private final String name;
    private final ArrayList<Transaction> transactionHistory = new ArrayList<>();
    @Setter private volatile double funds;
    @Setter private Map<Stock,Long> ownedStocks;

    /**
     * Constructor for the trader.
     * @param id The ID of the trader.
     * @param name The name of the trader.
     * @param funds The amount of funds the trader has.
     * @param ownedStocks The stocks that the trader owns By symbol and amount.
     */
    public Trader(String id, String name, double funds, Map<Stock, Long> ownedStocks) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.ownedStocks = ownedStocks;
    }

    /**
     * Method to add shares to a trader.
     * @param stock Stock for which to add shares
     * @param amount Amount of shares to add
     */
    public void addShares(Stock stock, long amount) {
        if (this.ownedStocks.containsKey(stock)) {
            long currentAmount = this.ownedStocks.get(stock);
            this.ownedStocks.put(stock,currentAmount + amount);
        } else {
            this.ownedStocks.put(stock, amount);
        }
    }

    /**
     * Method to remove shares from a trader.
     * @param stock Stock for which to remove shares
     * @param amount Amount of shares to remove
     */
    public void removeShares(Stock stock, long amount) {
        long currentAmount = this.ownedStocks.get(stock);
        if (currentAmount - amount <= 0) {
            this.ownedStocks.remove(stock);
        } else {
            this.ownedStocks.put(stock,currentAmount - amount);
        }
    }

    /**
     * Method to add funds to a trader.
     * @param amount Amount of funds to add
     */
    public void addFunds(double amount) {
        this.funds = this.funds + amount;
    }

    /**
     * Method to remove funds from a trader.
     * @param amount Amount of funds to remove
     */
    public void removeFunds(double amount) {
        this.funds = Math.max(this.funds - amount, 0);
    }

    /**
     * Method that returns the amount of shares of a stock the trader has.
     * @param stock Stock to get amount of shares of
     * @return Amount of shares owned by trader
     */
    public long getShares(Stock stock) {
        if (this.ownedStocks.containsKey(stock)) {
            return this.getOwnedStocks().get(stock);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Trader)) {
            return false;
        }
        Trader trader = (Trader)o;
        return trader.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}