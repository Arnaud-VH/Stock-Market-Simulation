package nl.rug.aoop.stockapplication.convertmarket.updating;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Updates the Stocks and the Traders periodically.
 */
public class UpdateAll {
    private final ScheduledExecutorService executer;
    private final UpdateStocks updateStocks;
    private final UpdateTraders updateTraders;

    /**
     * Constructor for the update all class .
     * @param updateStocks The stock updater.
     * @param updateTraders The trader updater.
     */
    public UpdateAll(UpdateStocks updateStocks, UpdateTraders updateTraders) {
        this.updateStocks = updateStocks;
        this.updateTraders = updateTraders;
        this.executer = Executors.newScheduledThreadPool(2);
    }

    /**
     * Method that schedules the stock updater every 2 seconds.
     */
    public void updateStocks() {
        executer.scheduleAtFixedRate(updateStocks::update, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Method that schedules the trader updater every 2 seconds.
     */
    public void updateTraders() {
        executer.scheduleAtFixedRate(updateTraders::update, 0, 2, TimeUnit.SECONDS);
    }

}
