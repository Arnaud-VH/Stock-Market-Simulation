package nl.rug.aoop.market;

import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.transaction.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
public class TestTransaction {

    @Test
    public void testConstructor() {
        Stock mockStock1 = Mockito.mock(Stock.class);
        Transaction transaction = new Transaction(mockStock1, 100, 50);
        assertNotNull(transaction.getStock());
        assertEquals(100, transaction.getStockPrice());
        assertEquals(50, transaction.getAmount());
        Mockito.verify(mockStock1).setPrice(100);
    }
}
