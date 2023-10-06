package nl.rug.aoop.networking.Command;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Robot.Robot;

import java.util.Map;

/**
 * Temporary command to test the functionality of the server.
 */
@Slf4j
public class MoveLeft extends AbstractRobotCommand{

    public MoveLeft(Robot robot) {
        super(robot);
    }

    @Override
    public void execute(Map<String, Object> params) {
        log.info("left");
    }
}
