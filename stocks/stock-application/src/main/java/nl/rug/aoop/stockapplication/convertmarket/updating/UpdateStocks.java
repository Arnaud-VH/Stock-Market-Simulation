package nl.rug.aoop.stockapplication.convertmarket.updating;

import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.model.StockExchangeData;
import nl.rug.aoop.model.stockdata.StockData;
import nl.rug.aoop.networkmarket.clientUpdater.ExchangeServer;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class that updates the stocks that are displayed in the view.
 */
@Slf4j
public class UpdateStocks {
    private volatile ExchangeServer exchange;
    private final StockExchangeData data;

    /**
     * Constructor for the update stocks class.
     * @param exchange The exchange where the stock data is stored.
     * @param data The data from the Yaml file.
     */
    public UpdateStocks(ExchangeServer exchange, StockExchangeData data) {
        this.exchange = exchange;
        this.data = data;
    }

    /**
     * Method that will be called by the Scheduler executor.
     */
    public void update() {
        ArrayList<Stock> exchangeStocks = exchange.getStocks();
        Map<String, StockData> viewStocks = data.getStocks();
        for (Stock s : exchangeStocks) {
            log.info("Stock info: " + s.getSymbol() + "Price: " + s.getPrice());
            viewStocks.get(s.getSymbol()).updateStockData(s.getSymbol(), s.getName(),
                    s.getOutstandingShares(), s.getPrice());
        }
    }

}
