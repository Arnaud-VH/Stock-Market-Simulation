package nl.rug.aoop.model.stockdata;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.StockDataModel;

/**
 * Class that reads in the stock data from the YAML File.
 */
@Data
@Slf4j
public class StockData implements StockDataModel {
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double initialPrice;

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSharesOutstanding() {
        return sharesOutstanding;
    }

    @Override
    public double getMarketCap() {
        return sharesOutstanding*initialPrice;
    }

    @Override
    public double getPrice() {
        return initialPrice;
    }

    /**
     * Updates the Stock Data.
     * @param symbol The symbol of the stock.
     * @param name The name of the stock.
     * @param sharesOutstanding The outstanding shares of the stock.
     * @param price The new price of the stock.
     */
    public void updateStockData(String symbol, String name, long sharesOutstanding, double price) {
        setInitialPrice(price);
        log.info("The StockData price is:" + price);
        setName(name);
        setSymbol(symbol);
        setSharesOutstanding(sharesOutstanding);
    }
}
