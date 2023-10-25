package nl.rug.aoop.market;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.exchange.Exchange;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.market.transaction.Ask;
import nl.rug.aoop.market.transaction.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestExchange {
    private Exchange exchange;
    private Stock mockStock1;
    private Stock mockStock2;
    private Trader mockTraderArnaud;
    private Trader mockTraderClement;

    @BeforeEach
    public void setup() {
        this.mockTraderArnaud = Mockito.mock(Trader.class);
        Mockito.when(mockTraderArnaud.getId()).thenReturn("T-RN0");
        Mockito.when(mockTraderArnaud.getFunds()).thenReturn(10000);
        Mockito.when(mockTraderArnaud.getShares(Mockito.any(Stock.class))).thenReturn(100);

        this.mockTraderClement = Mockito.mock(Trader.class);
        Mockito.when(mockTraderClement.getId()).thenReturn("T-CLMN7");
        Mockito.when(mockTraderClement.getFunds()).thenReturn(50000); // logically mock trader clement is wealthier
        Mockito.when(mockTraderClement.getShares(Mockito.any(Stock.class))).thenReturn(100);


        this.mockStock1 = Mockito.mock(Stock.class);
        Mockito.when(mockStock1.getSymbol()).thenReturn("MS1");

        this.mockStock2 = Mockito.mock(Stock.class);
        Mockito.when(mockStock2.getSymbol()).thenReturn("MS2");

        this.exchange = new Exchange(new ArrayList<>(Arrays.asList(mockStock1,mockStock2)),
                new ArrayList<>(Arrays.asList(mockTraderArnaud,mockTraderClement)));
    }

    @Test
    public void testPlaceUnresolvableAsk() {
        Ask ask = new Ask (mockTraderArnaud,mockStock1, 100, 50);
        exchange.placeAsk(ask);
        assertTrue(exchange.getAsks().get(mockStock1).contains(ask));
    }

    @Test
    public void testEqualAmountsTrade() {
        Ask ask = new Ask (mockTraderArnaud,mockStock1, 100, 50);
        exchange.placeAsk(ask);
        Bid bid = new Bid(mockTraderClement,mockStock1, 100, 50);
        exchange.placeBid(bid);
        assertFalse(exchange.getAsks().get(mockStock1).contains(ask));
        Mockito.verify(mockTraderArnaud).addShares(mockStock1, 100);
        Mockito.verify(mockTraderArnaud).removeFunds(50*100);
        Mockito.verify(mockTraderClement).addFunds(50*100);
        Mockito.verify(mockTraderClement).removeShares(mockStock1, 100);
    }

    @Test
    public void UnequalAmountsTrade() {
        Ask ask = new Ask (mockTraderArnaud, mockStock1, 20, 50);
        exchange.placeAsk(ask);
        Bid bid = new Bid(mockTraderClement, mockStock1, 100, 50);
        exchange.placeBid(bid);
        assertFalse(exchange.getAsks().get(mockStock1).contains(ask));
        assertTrue(exchange.getBids().get(mockStock1).contains(bid));
        assertTrue(bid.getShares() == 80 && bid.getPrice() == 50);
        Mockito.verify(mockTraderArnaud).addShares(mockStock1, 20);
        Mockito.verify(mockTraderArnaud).removeFunds(50*20);
        Mockito.verify(mockTraderClement).addFunds(50*20);
        Mockito.verify(mockTraderClement).removeShares(mockStock1, 20);
    }

    // TODO test trade when prices are not equal
    @Test
    public void InvalidAsk() {
        Ask ask = new Ask (mockTraderArnaud, mockStock1, 50, 1000);
        exchange.placeAsk(ask);
        Bid bid = new Bid (mockTraderClement, mockStock1, 50, 1000);
        exchange.placeBid(bid);
        assertTrue(exchange.getAsks(mockStock1).isEmpty());
        assertEquals(50, bid.getShares());
        assertTrue(exchange.getBids(mockStock1).contains(bid));
    }

    @Test
    public void InvalidBid() {
        Ask ask = new Ask (mockTraderArnaud, mockStock1, 500, 1);
        exchange.placeAsk(ask);
        Bid bid = new Bid (mockTraderClement, mockStock1, 500, 1);
        exchange.placeBid(bid);
        assertTrue(exchange.getBids(mockStock1).isEmpty());
        assertEquals(500, ask.getShares());
        assertTrue(exchange.getAsks(mockStock1).contains(ask));
    }

    @Test
    public void testDifferentPricesTrade() {
        Ask ask = new Ask (mockTraderArnaud,mockStock1, 100, 50);
        exchange.placeAsk(ask);
        Bid bid = new Bid(mockTraderClement,mockStock1, 100, 40);
        exchange.placeBid(bid);
        assertFalse(exchange.getAsks().get(mockStock1).contains(ask));
        Mockito.verify(mockTraderArnaud).addShares(mockStock1, 100);
        Mockito.verify(mockTraderArnaud).removeFunds(50*100);
        Mockito.verify(mockTraderClement).addFunds(50*100);
        Mockito.verify(mockTraderClement).removeShares(mockStock1, 100);
    }
}

