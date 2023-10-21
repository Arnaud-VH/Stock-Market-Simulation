package nl.rug.aoop.market;

import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class TestTrader {
    private Trader trader1;
    private Stock mockStock1;

    @BeforeEach
    public void setUp() {
        Map<Stock, Integer> stockMap = new HashMap<>();
        this.mockStock1 = Mockito.mock(Stock.class);
        Mockito.when(mockStock1.getName()).thenReturn("mockStock1");
        stockMap.put(mockStock1, 10);
        trader1 = new Trader("1", "Arnaud", 100, stockMap);
    }

    @Test
    public void testRemoveShares() {
        trader1.removeShares(mockStock1, 5);
        assertEquals(5, trader1.getShares(mockStock1));
    }

    @Test
    public void testRemoveAllShares() {
        trader1.removeShares(mockStock1, 11);
        assertTrue(trader1.getOwnedStocks().isEmpty());
    }

    @Test
    public void testAddShares() {
        trader1.addShares(mockStock1, 5);
        assertEquals(15, trader1.getShares(mockStock1));
    }

    @Test
    public void testAddSharesNewStock() {
        Stock mockStock2 = Mockito.mock(Stock.class);
        trader1.addShares(mockStock2, 10);
        assertEquals(2, trader1.getOwnedStocks().size());
        assertEquals(10, trader1.getShares(mockStock2));
    }

}
