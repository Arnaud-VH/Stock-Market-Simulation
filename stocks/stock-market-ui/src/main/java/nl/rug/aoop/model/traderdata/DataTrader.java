package nl.rug.aoop.model.traderdata;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.TraderDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that allows the yaml trader data to be loaded in.
 */
@Data
@Slf4j
public class DataTrader implements TraderDataModel {
    private String id;
    private String name;
    private double funds;
    private StockPortfolio stockPortfolio;

    @Override
    public List<String> getOwnedStocks() {
        List<String> ownedStocks = new ArrayList<>();
        for (Map.Entry<String, Long> entry : stockPortfolio.getOwnedShares().entrySet()) {
            ownedStocks.add(entry.getKey());
        }
        return ownedStocks;
    }

    public Map<String, Long> getOwnedShares() {
        return stockPortfolio.getOwnedShares();
    }

    /**
     * Update the Data Trader.
     * @param id ID of the trader.
     * @param name Name of the trader.
     * @param funds New funds of the trader.
     * @param stockPortfolio the new stock portfolio of the trader.
     */
    public void updateTrader(String id, String name, double funds, StockPortfolio stockPortfolio) {
        setId(id);
        setName(name);
        setFunds(funds);
        setStockPortfolio(stockPortfolio);
    }
}
