package nl.rug.aoop.messagequeue.CommandHandler;

import nl.rug.aoop.messagequeue.CommandHandler.Commands.MqPutCommand;
import nl.rug.aoop.messagequeue.Queues.MessageQueue;

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
