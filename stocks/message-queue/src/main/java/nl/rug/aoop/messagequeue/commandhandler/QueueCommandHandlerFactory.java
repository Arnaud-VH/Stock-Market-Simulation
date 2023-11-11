package nl.rug.aoop.messagequeue.commandhandler;

import nl.rug.aoop.messagequeue.commandhandler.commands.MqPutCommand;
import nl.rug.aoop.messagequeue.queues.MessageQueue;

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
