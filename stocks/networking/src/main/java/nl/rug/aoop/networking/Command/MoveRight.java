package nl.rug.aoop.networking.Command;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Robot.Robot;

import java.util.Map;

@Slf4j
public class MoveRight extends AbstractRobotCommand{

    public MoveRight(Robot robot) {
        super(robot);
    }

    @Override
    public void execute(Map<String, Object> params) {
        log.info("right");
    }
}
