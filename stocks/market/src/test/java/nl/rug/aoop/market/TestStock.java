package nl.rug.aoop.market;

import nl.rug.aoop.market.Stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestStock {

    private Stock stock1;
    @BeforeEach
    public void setUp() {
        this.stock1 = new Stock("AAPL", 50, "Apple", 10);
    }

    @Test
    public void testConstructor() {
        assertNotNull(stock1.getSymbol());
        assertNotNull(stock1.getName());
    }
    @Test
    public void testUpdateMarketCap() {
        stock1.setPrice(55);
        assertEquals(550, stock1.getMarketCap());
    }
}