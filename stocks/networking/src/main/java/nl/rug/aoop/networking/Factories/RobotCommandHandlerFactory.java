package nl.rug.aoop.networking.Factories;

import nl.rug.aoop.networking.Command.CommandHandler;
import nl.rug.aoop.networking.Command.MoveLeft;
import nl.rug.aoop.networking.Command.MoveRight;
import nl.rug.aoop.networking.Robot.Robot;

public class RobotCommandHandlerFactory implements AbstractCommandHandlerFactory{

    private final Robot robot;

    public RobotCommandHandlerFactory(Robot robot) {
        this.robot = robot;
    }

    @Override
    public CommandHandler createCommandHandler() {
        CommandHandler commandHandler = CommandHandler.getInstance();
        commandHandler.registerCommand("move.left", new MoveLeft(robot));
        commandHandler.registerCommand("move.right", new MoveRight(robot));
        return commandHandler;
    }
}
