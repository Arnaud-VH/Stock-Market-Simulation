module messagequeue {
    exports nl.rug.aoop.messagequeue.producers;
    exports nl.rug.aoop.messagequeue.queues;
    exports nl.rug.aoop.messagequeue.messagehandlers;
    exports nl.rug.aoop.messagequeue.commandhandler;
    exports nl.rug.aoop.messagequeue.consumers;
    requires static lombok;
    requires com.google.gson;
    requires org.slf4j;
    requires command;
    requires networking;
    requires org.mockito;
}