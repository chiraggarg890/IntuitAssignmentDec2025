package org.intuit.analysis;

import org.intuit.analysis.model.Sale;
import org.intuit.analysis.reader.CsvReader;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest {

    @Test
    public void testReadSales_parsesCsvCorrectly() throws Exception {
        // Arrange: create a temporary CSV file
        String csvContent = String.join("\n",
                "sale_id,date,product_id,product_name,category,unit_price,quantity,region,salesperson",
                "1,2024-01-01,P1,Product A,Cat1,100.5,2,APAC,Alice",
                "2,2024-02-10,P2,Product B,Cat2,200.0,3,EMEA,Bob"
        );

        Path tempFile = Files.createTempFile("sales_test", ".csv");
        Files.writeString(tempFile, csvContent);

        // Act
        List<Sale> sales = CsvReader.readSales(tempFile.toString());

        // Assert
        assertEquals(2, sales.size());

        Sale first = sales.get(0);
        assertEquals(LocalDate.of(2024, 1, 1), first.getDate());
        assertEquals("P1", first.getProductId());
        assertEquals("Cat1", first.getCategory());
        assertEquals(2, first.getQuantity());
        assertEquals(100.5, first.getUnitPrice());
        assertEquals("APAC", first.getRegion());
        assertEquals(201.0, first.revenue()); // 100.5 * 2

        Files.deleteIfExists(tempFile);
    }
}
