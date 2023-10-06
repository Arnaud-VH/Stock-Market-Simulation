package nl.rug.aoop.networking.Command;

import java.util.Map;

/**
 * Command interface used to execute commands given by the client.
 */
public interface Command {

    /**
     * Execute method that has to be implemented by all classes that follow the command pattern.
     */
    void execute(Map<String,Object> params);
}
