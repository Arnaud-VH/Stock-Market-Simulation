package nl.rug.aoop.command.Command;

import java.util.Map;

public interface CommandHandler {
    void execute(String commandKey, Map<String, Object> args);
}
