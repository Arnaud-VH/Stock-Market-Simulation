package nl.rug.aoop.networkmarket.TraderClient.TraderCommandHandler.Commands;

import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.networkmarket.TraderClient.TraderClient;

import java.util.Map;

/**
 * Class that handles the receiving of the clientID as a command.
 */
public class ClientIDCommand implements Command {
    private final TraderClient traderClient;

    /**
     * Constructor for the ClientID Command.
     * @param traderClient The traderClient that gets the ID.
     */
    public ClientIDCommand(TraderClient traderClient) {
        this.traderClient = traderClient;
    }

    @Override
    public void execute(Map<String, Object> params) {
        traderClient.register(Integer.parseInt((String)params.get("body")));
    }
}
