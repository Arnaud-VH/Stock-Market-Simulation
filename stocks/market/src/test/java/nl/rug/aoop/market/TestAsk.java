package nl.rug.aoop.market;

import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
public class TestAsk {

    @Test
    public void testConstructor() {
        Trader mockTrader = Mockito.mock(Trader.class);
        Stock mockStock = Mockito.mock(Stock.class);
        Ask ask = new Ask(mockTrader, mockStock, 5, 10);
        assertNotNull(ask.getStock());
        assertNotNull(ask.getTrader());
        assertEquals(10, ask.getPrice());
        assertEquals(5, ask.getShares());
    }
}
