package org.intuit.analysis;

import org.intuit.analysis.model.Sale;
import org.intuit.analysis.service.SalesAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

 import static org.junit.jupiter.api.Assertions.*;

public class SalesAnalyzerTest {

    private List<Sale> sales;
    private SalesAnalyzer analyzer;

    @BeforeEach
    public void setup() {
        analyzer = new SalesAnalyzer();

        sales = List.of(
                new Sale("1", LocalDate.of(2024,1,1), "P1","ProdA","Cat1", 100, 2,"APAC","A"),
                new Sale("2", LocalDate.of(2024,1,1), "P2","ProdB","Cat2", 200, 3,"EMEA","B"),
                new Sale("3", LocalDate.of(2024,2,1), "P1","ProdA","Cat1", 150, 1,"APAC","A"),
                new Sale("4", LocalDate.of(2024,2,5), "P3","ProdC","Cat3", 300, 2,"US","C")
        );
    }

    @Test
    public void testTotalRevenue() {
        double expected =
                100*2 + 200*3 + 150*1 + 300*2;
        assertEquals(expected, analyzer.getTotalRevenue(sales));
    }

    @Test
    public void testTotalUnits() {
        assertEquals(2+3+1+2, analyzer.getTotalUnits(sales));
    }

    @Test
    public void testRevenueByRegion() {
        Map<String, Double> map = analyzer.getRevenueByRegion(sales);
        assertEquals(100*2 + 150*1, map.get("APAC"));
        assertEquals(200*3, map.get("EMEA"));
        assertEquals(300*2, map.get("US"));
    }

    @Test
    public void testRevenueByProduct() {
        Map<String, Double> map = analyzer.getRevenueByProduct(sales);
        assertEquals(100*2 + 150*1, map.get("P1"));
        assertEquals(200*3, map.get("P2"));
        assertEquals(300*2, map.get("P3"));
    }

    @Test
    public void testUnitsByProduct() {
        Map<String, Long> map = analyzer.getUnitsByProduct(sales);
        assertEquals(3L, map.get("P1")); // 2 + 1
        assertEquals(3L, map.get("P2"));
        assertEquals(2L, map.get("P3"));
    }

    @Test
    public void testMonthlyRevenue() {
        Map<YearMonth, Double> map = analyzer.getMonthlyRevenue(sales);
        assertEquals(100*2 + 200*3, map.get(YearMonth.of(2024,1)));
        assertEquals(150*1 + 300*2, map.get(YearMonth.of(2024,2)));
    }

    @Test
    public void testDailyRevenue() {
        Map<LocalDate, Double> map = analyzer.getDailyRevenue(sales);
        assertEquals(100*2 + 200*3, map.get(LocalDate.of(2024,1,1)));
        assertEquals(150*1, map.get(LocalDate.of(2024,2,1)));
        assertEquals(300*2, map.get(LocalDate.of(2024,2,5)));
    }

    @Test
    public void testRevenueByCategory() {
        Map<String, Double> map = analyzer.getRevenueByCategory(sales);
        assertEquals(100*2 + 150*1, map.get("Cat1"));
        assertEquals(200*3, map.get("Cat2"));
        assertEquals(300*2, map.get("Cat3"));
    }

    @Test
    public void testAverageRevenue() {
        double sum = 100*2 + 200*3 + 150*1 + 300*2;
        assertEquals(sum / sales.size(), analyzer.getAverageRevenuePerSale(sales));
    }

    @Test
    public void testHighestSale() {
        assertEquals(300*2, analyzer.getHighestSale(sales).get().revenue());
    }

    @Test
    public void testLowestSale() {
        assertEquals(150, analyzer.getLowestSale(sales).get().revenue());
    }

    @Test
    public void testTopNProducts() {
        List<Map.Entry<String, Double>> top = analyzer.getTopNProductsByRevenue(sales, 1);
        assertEquals("P2", top.get(0).getKey());
    }

    @Test
    public void testTopRegions() {
        List<Map.Entry<String, Double>> top = analyzer.getTopRegions(sales, 1);
        assertEquals("EMEA", top.get(0).getKey());
    }

    @Test
    public void testRevenueBySalesperson() {
        Map<String, Double> map = analyzer.getRevenueBySalesperson(sales);
        assertEquals(100*2 + 150*1, map.get("A"));
        assertEquals(200*3, map.get("B"));
        assertEquals(300*2, map.get("C"));
    }

    @Test
    public void testHighValueSales() {
        List<Sale> big = analyzer.getHighValueSales(sales, 600);
        assertEquals(2, big.size());
    }

    @Test
    public void testSaleCountByRegion() {
        Map<String, Long> map = analyzer.getSaleCountByRegion(sales);
        assertEquals(2L, map.get("APAC"));
        assertEquals(1L, map.get("EMEA"));
        assertEquals(1L, map.get("US"));
    }

    @Test
    public void testCategorySummary() {
        Map<String, Map<String, Number>> summary = analyzer.getCategorySummary(sales);

        assertEquals(3L, summary.get("Cat1").get("units"));
        assertEquals(350.0, summary.get("Cat1").get("revenue"));
    }

    @Test
    public void testProductSummary() {
        Map<String, Map<String, Number>> summary = analyzer.getProductSummary(sales);

        Map<String, Number> p1 = summary.get("P1");
        assertEquals(3L, p1.get("units"));
        assertEquals(100.0*2 + 150, p1.get("revenue"));
    }

    @Test
    public void testMonthlySaleCount() {
        Map<YearMonth, Long> map = analyzer.getMonthlySaleCount(sales);
        assertEquals(2L, map.get(YearMonth.of(2024,1)));
        assertEquals(2L, map.get(YearMonth.of(2024,2)));
    }

    @Test
    public void testGenericGroupBy() {
        Map<String, List<Sale>> map = analyzer.groupBy(sales, Sale::getRegion);
        assertEquals(2, map.get("APAC").size());
        assertEquals(1, map.get("EMEA").size());
    }
}