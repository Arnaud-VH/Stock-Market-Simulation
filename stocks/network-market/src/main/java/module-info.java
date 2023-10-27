module network.market {
    exports nl.rug.aoop.networkmarket.serialiser;
    exports nl.rug.aoop.networkmarket.traderclient;
    exports nl.rug.aoop.networkmarket.exchangeserver.clientUpdater;
    //exports nl.rug.aoop.networkmarket;
    exports nl.rug.aoop.networkmarket.exchangeserver;
    requires lombok;
    requires command;
    requires market;
    requires org.mockito;
    requires org.slf4j;
    requires messagequeue;
    requires networking;
}