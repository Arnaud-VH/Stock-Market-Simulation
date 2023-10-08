package nl.rug.aoop.networking.Handlers;

import java.util.Map;

public interface CommandHandler {
    void execute(String commandKey, Map<String, Object> args);
}
