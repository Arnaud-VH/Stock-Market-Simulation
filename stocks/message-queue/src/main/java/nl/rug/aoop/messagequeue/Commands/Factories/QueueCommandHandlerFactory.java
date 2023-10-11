package nl.rug.aoop.messagequeue.Commands.Factories;

import nl.rug.aoop.command.Command.AbstractCommandHandlerFactory;
import nl.rug.aoop.messagequeue.Commands.MqPutCommand;
import nl.rug.aoop.messagequeue.Commands.QueueCommandHandler;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;

/**
 * Class that creates the QueueCommands using the Factory Design Pattern.
 */
public class QueueCommandHandlerFactory implements AbstractCommandHandlerFactory {

    private MessageQueue queue;

    /**
     * The constructor for the Queue command handler factory.
     * @param queue The queue on which the queue commands will be executed.
     */
    public QueueCommandHandlerFactory(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public QueueCommandHandler createCommandHandler() {
        QueueCommandHandler queueCommandHandler = QueueCommandHandler.getInstance();
        queueCommandHandler.registerCommand("MQPut", new MqPutCommand(queue));
        return queueCommandHandler;
    }
}
