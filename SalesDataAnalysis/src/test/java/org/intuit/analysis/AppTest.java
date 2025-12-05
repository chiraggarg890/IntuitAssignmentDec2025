package org.intuit.analysis;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AppTest {

    @Test
    public void testMain_runsWithoutException() throws Exception {
        // Arrange: minimal valid CSV file
        String csvContent = String.join("\n",
                "sale_id,date,product_id,product_name,category,unit_price,quantity,region,salesperson",
                "1,2024-01-01,P1,Product A,Cat1,100.0,2,APAC,Alice"
        );

        Path tempFile = Files.createTempFile("app_sales_test", ".csv");
        Files.writeString(tempFile, csvContent);

        String[] args = { tempFile.toString() };

        // Act + Assert: main should run without throwing any exception
        assertDoesNotThrow(() -> App.main(args));

        Files.deleteIfExists(tempFile);
    }
}
