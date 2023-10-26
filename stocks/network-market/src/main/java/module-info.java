module network.market {
    exports nl.rug.aoop.networkmarket.serialiser;
    exports nl.rug.aoop.networkmarket.TraderClient;
    exports nl.rug.aoop.networkmarket.clientUpdater;
    requires lombok;
    requires command;
    requires market;
    requires org.mockito;
    requires org.slf4j;
    requires messagequeue;
    requires networking;
}