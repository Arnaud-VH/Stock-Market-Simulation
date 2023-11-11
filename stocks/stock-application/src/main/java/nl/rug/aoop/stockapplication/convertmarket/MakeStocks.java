package nl.rug.aoop.stockapplication.convertmarket;

import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeData;

import java.util.ArrayList;

/**
 * Creates the market stocks from the Yaml loaded stocks.
 */
public class MakeStocks {
    private final StockExchangeData data;

    /**
     * Constructor to make the stocks.
     * @param data The loaded Yaml stock exchange data.
     */
    public MakeStocks(StockExchangeData data) {
        this.data = data;
    }

    /**
     * Creates the stocks that are in the exchange.
     * @return An arraylist of stocks.
     */
    public ArrayList<Stock> createStocks() {
        ArrayList<Stock> stockList = new ArrayList<>();
        for (int i = 0; i < data.getNumberOfStocks(); i++) {
            StockDataModel stock = data.getStockByIndex(i);
            Stock newStock = new Stock(stock.getSymbol(), stock.getPrice(), stock.getName(),
                    stock.getSharesOutstanding());
            stockList.add(newStock);
        }

        return stockList;
    }

}
