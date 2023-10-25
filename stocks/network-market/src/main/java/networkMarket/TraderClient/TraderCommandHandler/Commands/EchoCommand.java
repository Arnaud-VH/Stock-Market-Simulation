package networkMarket.TraderClient.TraderCommandHandler.Commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;

import java.util.Map;

/**
 * Command that echos what the server sends.
 */
@Slf4j
public class EchoCommand implements Command {

    @Override
    public void execute(Map<String, Object> params) {
        log.info("TraderClient received echo from server " + (String)params.get("body"));
    }
}
