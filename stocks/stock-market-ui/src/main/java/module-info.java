module stock.market.ui {
    requires static lombok;
    exports nl.rug.aoop.model;
    exports nl.rug.aoop.initialization;
    requires org.slf4j;
    requires java.desktop;
    requires com.formdev.flatlaf;
    requires util;

    opens nl.rug.aoop.model.stockdata to com.fasterxml.jackson.databind;
    opens nl.rug.aoop.model.traderdata to com.fasterxml.jackson.databind;
    exports nl.rug.aoop.model.stockdata;
    exports nl.rug.aoop.model.traderdata;
}