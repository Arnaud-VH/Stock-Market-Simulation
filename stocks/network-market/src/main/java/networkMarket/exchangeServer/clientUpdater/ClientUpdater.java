package networkMarket.exchangeServer.clientUpdater;

import lombok.extern.slf4j.Slf4j;
import networkMarket.MarketSerializer;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.networking.Server.ClientHandler;
import nl.rug.aoop.networking.Server.Server;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

@Slf4j
public class ClientUpdater implements Runnable {
    Exchange exchange;
    Server server;
    public ClientUpdater (Exchange exchange, Server server) {
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
