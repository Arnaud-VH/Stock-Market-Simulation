package networkMarket.TraderClient.TraderCommandHandler.Commands;

import lombok.extern.slf4j.Slf4j;
import networkMarket.MarketSerializer;
import networkMarket.TraderClient.TraderClient;
import nl.rug.aoop.command.Command.Command;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class UpdateCommand implements Command {
    private volatile TraderClient trader;

    public UpdateCommand(TraderClient trader) {
        this.trader = trader;
    }

    @Override
    public void execute(Map<String, Object> params) {
        try {
            ArrayList<Serializable> arrayList = MarketSerializer.fromString((String)params.get("body"), ArrayList.class);
            ArrayList<Stock> newStocks = (ArrayList<Stock>)arrayList.get(0);
            log.info("Calls the update command");
            Trader networkedTrader = (Trader)arrayList.get(1);
            log.info("The network trader has funds: " + networkedTrader.getFunds());
            log.info("Normal trader has funds BEFORE:" + trader.getFunds());
            this.trader.updateTrader(networkedTrader.getFunds(), networkedTrader.getOwnedStocks());
            log.info("Normal trader AFTER has funds: " + trader.getFunds());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Could not execute command because deserialization of arrayList went wrong: ", e);
        }

    }
}
