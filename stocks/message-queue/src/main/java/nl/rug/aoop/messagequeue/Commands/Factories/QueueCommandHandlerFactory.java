package nl.rug.aoop.messagequeue.Commands.Factories;

import nl.rug.aoop.command.Command.AbstractCommandHandlerFactory;
import nl.rug.aoop.messagequeue.Commands.MqPutCommand;
import nl.rug.aoop.messagequeue.Commands.QueueCommandHandler;

public class QueueCommandHandlerFactory implements AbstractCommandHandlerFactory {

    //Constructor for the Factory pattern brings the Queue into here, so that the MqPutCommand knows what Queue to use.

    @Override
    public QueueCommandHandler createCommandHandler() {
        QueueCommandHandler queueCommandHandler = QueueCommandHandler.getInstance();
        queueCommandHandler.registerCommand("MQPut", new MqPutCommand());
        return queueCommandHandler;
    }
}
