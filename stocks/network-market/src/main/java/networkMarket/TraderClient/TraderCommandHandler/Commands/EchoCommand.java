package networkMarket.TraderClient.TraderCommandHandler.Commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.Command;

import java.util.Map;

@Slf4j
public class EchoCommand implements Command {

    @Override
    public void execute(Map<String, Object> params) {
        log.info("TraderClient received echo from server " + (String)params.get("body"));
    }
}
