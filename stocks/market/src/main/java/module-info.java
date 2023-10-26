module market {
    exports nl.rug.aoop.market.trader;
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;

    opens nl.rug.aoop.market.trader to com.fasterxml.jackson.databind;
    exports nl.rug.aoop.market.exchange;
    exports nl.rug.aoop.market.transaction;
    exports nl.rug.aoop.market.stock;
}