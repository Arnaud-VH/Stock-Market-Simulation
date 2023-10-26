package nl.rug.aoop.stockapplication.convertmarket;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.model.StockExchangeData;
import nl.rug.aoop.model.traderdata.DataTrader;
import nl.rug.aoop.model.traderdata.StockPortfolio;
import nl.rug.aoop.networkmarket.clientUpdater.ExchangeServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that updates the traders displayed in the view.
 */
@Slf4j
public class UpdateTraders {
    private volatile ExchangeServer exchangeServer;
    private final StockExchangeData data;

    /**
     * Constructor for the update trader class.
     * @param exchangeServer The exchange server where the trader data is stored.
     * @param data The data that is loaded from the YAMl file.
     */
    public UpdateTraders(ExchangeServer exchangeServer, StockExchangeData data) {
        this.exchangeServer = exchangeServer;
        this.data = data;
    }

    /**
     * Method that will be scheduled to execute every 2 seconds and updates the trader information in the view.
     */
    public void update() {
        ArrayList<Trader> traderList = exchangeServer.getTraders();
        List<DataTrader> viewTraders = data.getTraders();
        int count = 0;
        for (Trader t : traderList) {
            Map<String, Long> ownedViewStocks = new HashMap<>();
            Map<Stock, Long> ownedStocks = t.getOwnedStocks();
            for (Stock s : ownedStocks.keySet()) {
                ownedViewStocks.put(s.getSymbol(), ownedStocks.get(s));
            }
            StockPortfolio stockPortfolio = new StockPortfolio(ownedViewStocks);
            log.info("View trader has funds: " + viewTraders.get(count).getFunds());
            viewTraders.get(count).updateTrader(t.getId(), t.getName(), t.getFunds(), stockPortfolio);
            count++;
        }

    }
}
