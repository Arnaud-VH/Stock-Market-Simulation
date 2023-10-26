package nl.rug.aoop.stockapplication.convertmarket;

import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.model.StockExchangeData;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.model.stockdata.StockData;
import nl.rug.aoop.model.traderdata.DataTrader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes the traders from the DataTrader. This is our converter.
 */
public class MakeTraders {
    private final StockExchangeData data;

    /**
     * Constructor for the MakeTraders class.
     * @param data The data that contains the DataTraders.
     */
    public MakeTraders(StockExchangeData data) {
        this.data = data;
    }

    /**
     * Creates an arraylist of regular traders.
     * @return Returns an arraylist of regular traders that can work with our market module.
     */
    public ArrayList<Trader> createTraders() {
        ArrayList<Trader> traderList = new ArrayList<>();
        for (int i = 0; i < data.getNumberOfTraders(); i++) {
            TraderDataModel trader = data.getTraderByIndex(i);
            DataTrader castedTrader = (DataTrader) trader;

            List<String> symbolList = trader.getOwnedStocks();
            Map<Stock, Long> ownedStocks = new HashMap<>();
            Map<String, Long> ownedShares = castedTrader.getOwnedShares();
            Map<String, StockData> stockMap = data.getStocks();

            for (String s : symbolList) {
                StockData stockData = stockMap.get(s);
                Stock newStock = new Stock(s, stockData.getPrice(), stockData.getName(),
                        stockData.getSharesOutstanding());
                ownedStocks.put(newStock, ownedShares.get(s));
            }

            Trader newTrader = new Trader(trader.getId(), trader.getName(), trader.getFunds(), ownedStocks);
            traderList.add(newTrader);
        }
        return traderList;
    }

}
