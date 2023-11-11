module trader.application {
    requires util;
    requires market;
    requires lombok;
    requires command;
    requires networking;
    requires messagequeue;
    requires network.market;
    requires stock.market.ui;
    requires stock.application;
}