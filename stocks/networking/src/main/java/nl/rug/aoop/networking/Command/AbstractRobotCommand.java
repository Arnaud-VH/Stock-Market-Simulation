package nl.rug.aoop.networking.Command;

import nl.rug.aoop.networking.Robot.Robot;

/**
 * These will be replaced by appropriate commands but we're leaving thme in right now to test our code.
 */
abstract class AbstractRobotCommand implements Command{
    protected Robot robot;

    AbstractRobotCommand(Robot robot) {
        this.robot = robot;
    }
}
