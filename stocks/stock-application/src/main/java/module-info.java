module stock.application {
    exports nl.rug.aoop.stockapplication.convertmarket;
    requires util;
    requires stock.market.ui;
    requires network.market;
    requires org.slf4j;
    requires market;
    requires lombok;
}