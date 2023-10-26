package nl.rug.aoop.traderapplication;

import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.model.StockExchangeData;
import nl.rug.aoop.networkmarket.TraderClient.TraderClient;
import nl.rug.aoop.stockapplication.convertmarket.MakeStocks;
import nl.rug.aoop.stockapplication.convertmarket.MakeTraders;
import nl.rug.aoop.traderapplication.randomordergenerator.RandomOrderGenerator;
import nl.rug.aoop.traderapplication.randomordergenerator.TraderBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Main for trader application.
 */
public class MainTraderApplication {

    /**
     * Main class.
     * @param args Arguments.
     * @throws IOException IOException.
     */
    public static void main(String[] args) throws IOException {
        StockExchangeData data = new StockExchangeData();
        data.load();

        MakeTraders makeTrader = new MakeTraders(data);
        MakeStocks makeStocks = new MakeStocks(data);
        ArrayList<Stock> allStocks = makeStocks.createStocks();

        //Initialise all the trader clients. Schedule the orders from the bots.
        for (Trader t : makeTrader.createTraders()) {
            TraderClient traderClient = new TraderClient(t.getId(), t.getName(), t.getFunds(), t.getOwnedStocks());
            traderClient.start();
            Random rand = new Random();
            RandomOrderGenerator orderGenerator = new RandomOrderGenerator(traderClient, allStocks, rand);
            TraderBot traderBot = new TraderBot(traderClient, orderGenerator);
            traderBot.placeOrder();
        }

    }
}
