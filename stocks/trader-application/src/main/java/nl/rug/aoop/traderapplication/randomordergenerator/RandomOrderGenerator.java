package nl.rug.aoop.traderapplication.randomordergenerator;

import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.networkmarket.traderclient.TraderClient;
import nl.rug.aoop.traderapplication.order.LimitOrder;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that creates random order that the traders can place.
 */
public class RandomOrderGenerator {
    private final TraderClient traderClient;
    private final ArrayList<Stock> stockList;
    private final Random rand;

    /**
     * Constructor for the random order generator.
     * @param traderClient The trader client that places the order.
     * @param stockList The list of stocks that are in the exchange.
     * @param rand Gets in an instance of when the trader application is created so there is a random seed.
     */
    public RandomOrderGenerator(TraderClient traderClient, ArrayList<Stock> stockList, Random rand) {
        this.traderClient = traderClient;
        this.stockList = stockList;
        this.rand = rand;
    }

    /**
     * Places an order onto the exchange that has been randomly generated.
     */
    public void getOrder() {
        double randomValue = Math.random();
        if (randomValue >= 0.5) {
            LimitOrder buyOrder = createBuyOrder();
            traderClient.placeAsk(buyOrder.getStock(), buyOrder.getStockAmount(), buyOrder.getPrice());
        } else {
            LimitOrder sellOrder = createSellOrder();
            traderClient.placeBid(sellOrder.getStock(), sellOrder.getStockAmount(), sellOrder.getPrice());
        }
    }

    private LimitOrder createBuyOrder() {
        int index = rand.nextInt(0, stockList.size());
        Stock buyStock = stockList.get(index);
        double funds = traderClient.getFunds();
        double price = buyStock.getPrice();
        long shares =  Math.round(price/funds);
        long buyShares = Math.max(1,shares/rand.nextInt(2,10));

        return new LimitOrder(traderClient.getId(), buyStock, buyShares,(price+(price*0.10)));
    }

    private LimitOrder createSellOrder() {
        int index = rand.nextInt(traderClient.getOwnedStocks().size());
        Object temp = traderClient.getOwnedStocks().keySet().toArray()[index];
        Stock ownedStock = (Stock) temp;
        long shares = traderClient.getOwnedStocks().get(ownedStock);
        double price = ownedStock.getPrice();
        long sellShares = Math.max(1,shares/rand.nextInt(2,10));

        return new LimitOrder(traderClient.getId(), ownedStock, sellShares, (price-(price*0.04)));
    }

}
