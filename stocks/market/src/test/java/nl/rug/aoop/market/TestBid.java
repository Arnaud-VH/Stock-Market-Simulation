package nl.rug.aoop.market;

import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.market.Transaction.Bid;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
public class TestBid {

    @Test
    public void testConstructor() {
        Trader mockTrader = Mockito.mock(Trader.class);
        Stock mockStock = Mockito.mock(Stock.class);
        Bid bid = new Bid(mockTrader, mockStock, 5, 10);
        assertNotNull(bid.getStock());
        assertNotNull(bid.getTrader());
        assertEquals(10, bid.getPrice());
        assertEquals(5, bid.getShares());
    }
}
