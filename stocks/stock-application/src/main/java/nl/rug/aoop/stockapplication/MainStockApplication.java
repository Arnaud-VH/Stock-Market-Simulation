package nl.rug.aoop.stockapplication;

import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.model.StockExchangeData;
import nl.rug.aoop.networkmarket.clientUpdater.ExchangeServer;
import nl.rug.aoop.stockapplication.convertmarket.*;

import java.io.IOException;

/**
 * Main class for the stock application.
 */
public class MainStockApplication {

    /**
     * Main to run the stock application.
     * @param args The terminal arguments.
     * @throws IOException IOException.
     */
    public static void main(String[] args) throws IOException {
        StockExchangeData data = new StockExchangeData();
        data.load();
        SimpleViewFactory view = new SimpleViewFactory();
        view.createView(data);

        MakeStocks stockMaker = new MakeStocks(data);
        MakeTraders makeTraders = new MakeTraders(data);
        ExchangeServer exchangeServer = new ExchangeServer(stockMaker.createStocks(), makeTraders.createTraders());
        UpdateStocks stockUpdater = new UpdateStocks(exchangeServer, data);
        UpdateTraders traderUpdater = new UpdateTraders(exchangeServer, data);
        UpdateAll updater = new UpdateAll(stockUpdater, traderUpdater);
        updater.updateStocks();
        updater.updateTraders();
        exchangeServer.start();
    }
}
