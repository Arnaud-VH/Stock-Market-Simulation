package networkMarket.exchangeServer.clientUpdater;

import lombok.extern.slf4j.Slf4j;
import networkMarket.MarketSerializer;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.networking.Server.Server;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Client updates sends the required information to the connected traders.
 */
@Slf4j
public class ClientUpdater implements Runnable {
    private Exchange exchange;
    private Server server;

    /**
     * Constructor for the client Updater class.
     * @param exchange The exchange on which the client updater works.
     * @param server The server to which the client updater is connected.
     */
    public ClientUpdater(Exchange exchange, Server server) {
        this.exchange = exchange;
        this.server = server;
    }

    @Override
    public void run() {
        log.info("Updating clients with latest exchange information");
        try {
            ArrayList<Serializable> exchangeInfo = new ArrayList<>();
            exchangeInfo.add(exchange.getStocks());
            exchangeInfo.add(exchange.getTraders());
            String info = MarketSerializer.toString(exchangeInfo);
            for (var entry : server.getClientHandlers().entrySet()) {
                entry.getValue().sendMessage(info);
            }
        } catch (IOException e) {
            log.error("Failed to serialize exchange information to send to clients");
        }
    }
}
