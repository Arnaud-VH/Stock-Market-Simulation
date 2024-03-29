package nl.rug.aoop.messagequeue.commandhandler.commands;

import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.Message;

import java.util.Map;

/**
 * Commands that puts a message into the Message Queue.
 */
public class MqPutCommand implements Command {
    private final MessageQueue queue;

    /**
     * The constructor for the MQPut command.
     * @param queue The Queue on which the command will be performed.
     */
    public MqPutCommand(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public void execute(Map<String, Object> params) {
        Message msg = Message.fromJson((String) params.get("body"));
        queue.enqueue(msg);
    }
}
