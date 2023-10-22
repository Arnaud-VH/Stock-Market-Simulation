package nl.rug.aoop.market.Trader;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The class representing a trader that buys and sells stocks.
 */
@Getter
public class Trader {
    private final String id;
    private final String name;
    private final List<Transaction> transactionHistory = new ArrayList<>();
    @Setter private int funds;
    @Setter private Map<Stock,Integer> ownedStocks;

    /**
     * Constructor for the trader.
     * @param id The ID of the trader.
     * @param name The name of the trader.
     * @param funds The amount of funds the trader has.
     * @param ownedStocks The stocks that the trader owns By symbol and amount.
     */
    public Trader(String id, String name, int funds, Map<Stock, Integer> ownedStocks) {
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
    public void addShares(Stock stock, int amount) {
        if (this.ownedStocks.containsKey(stock)) {
            int currentAmount = this.ownedStocks.get(stock);
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
    public void removeShares(Stock stock, int amount) {
        int currentAmount = this.ownedStocks.get(stock);
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
    public void addFunds(int amount) {
        this.funds = this.funds + amount;
    }

    /**
     * Method to remove funds from a trader.
     * @param amount Amount of funds to remove
     */
    public void removeFunds(int amount) {
        this.funds = Math.max(this.funds - amount, 0);
    }

    /**
     * Method that returns the amount of shares of a stock the trader has.
     * @param stock Stock to get amount of shares of
     * @return Amount of shares owned by trader
     */

    public int getShares(Stock stock) {
        if (this.ownedStocks.containsKey(stock)) {
            return this.getOwnedStocks().get(stock);
        }
        return 0;
    }

}