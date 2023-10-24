package networkMarket.TraderClient.TraderCommandHandler.Commands;

import networkMarket.TraderClient.TraderClient;
import nl.rug.aoop.command.Command.Command;

import java.util.Map;

public class ClientIDCommand implements Command {
    private final TraderClient traderClient;
    public ClientIDCommand (TraderClient traderClient) {
        this.traderClient = traderClient;
    }
    @Override
    public void execute(Map<String, Object> params) {
        traderClient.register(Integer.parseInt((String)params.get("body")));
    }
}
