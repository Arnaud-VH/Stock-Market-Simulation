package nl.rug.aoop.traderapplication.randomordergenerator;

import nl.rug.aoop.networkmarket.TraderClient.TraderClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The trader bot is a client that randomly places order onto the exchange server.
 */
public class TraderBot {
    private final RandomOrderGenerator randomOrderGenerator;
    private ScheduledExecutorService executer;

    /**
     * Constructor for the trader bot.
     * @param randomOrderGenerator The class that allows for random order to be generated.
     */
    public TraderBot(RandomOrderGenerator randomOrderGenerator) {
        this.randomOrderGenerator = randomOrderGenerator;
        this.executer = Executors.newScheduledThreadPool(1);
    }

    /**
     * Method that is called which schedules the placement of an order every 2 seconds. 
     */
    public void placeOrder() {
        executer.scheduleAtFixedRate(randomOrderGenerator::getOrder, 1, 2, TimeUnit.SECONDS);
    }

}
