package nl.rug.aoop.model;

import lombok.Data;
import nl.rug.aoop.model.stockdata.StockData;
import nl.rug.aoop.model.traderdata.DataTrader;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * StockExchange data class that gets all the data from the Yaml files and stores it.
 */
@Data
public class StockExchangeData implements StockExchangeDataModel{
    private Map<String, StockData> stocks;
    private List<DataTrader> traders;

    @Override
    public StockDataModel getStockByIndex(int index) {
        Object keyAtIndex = stocks.keySet().toArray()[index];
        Object value = stocks.get(keyAtIndex);
        return (StockDataModel) value;
    }

    /**
     * Loads in the data from the YAML file.
     * @throws IOException Throws an exception if it has problems with the YAML files.
     */
    public void load() throws IOException {
        YamlLoader loader = new YamlLoader(Path.of("data/stocks.yaml"));
        YamlLoader loader2 = new YamlLoader(Path.of("data/traders.yaml"));
        stocks = loader.load(StockExchangeData.class).getStocks();
        traders = loader2.load(StockExchangeData.class).getTraders();
    }

    @Override
    public int getNumberOfStocks() {
        return stocks.size();
    }

    @Override
    public TraderDataModel getTraderByIndex(int index) {
        return traders.get(index);
    }

    @Override
    public int getNumberOfTraders() {
        return traders.size();
    }
}
