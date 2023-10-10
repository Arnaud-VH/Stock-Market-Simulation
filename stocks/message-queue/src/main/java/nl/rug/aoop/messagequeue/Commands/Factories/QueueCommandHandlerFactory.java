package nl.rug.aoop.messagequeue.Commands.Factories;

import nl.rug.aoop.command.Command.AbstractCommandHandlerFactory;
import nl.rug.aoop.messagequeue.Commands.MqPutCommand;
import nl.rug.aoop.messagequeue.Commands.QueueCommandHandler;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;

public class QueueCommandHandlerFactory implements AbstractCommandHandlerFactory {

    private MessageQueue queue;
    //Constructor for the Factory pattern brings the Queue into here, so that the MqPutCommand knows what Queue to use.
    public QueueCommandHandlerFactory (MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public QueueCommandHandler createCommandHandler() {
        QueueCommandHandler queueCommandHandler = QueueCommandHandler.getInstance();
        queueCommandHandler.registerCommand("MQPut", new MqPutCommand(queue));
        return queueCommandHandler;
    }
}
