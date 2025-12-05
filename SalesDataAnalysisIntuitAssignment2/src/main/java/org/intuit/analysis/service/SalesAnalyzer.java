package org.intuit.analysis.service;

import org.intuit.analysis.model.Sale;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@code SalesAnalyzer} class provides a complete suite of analytical
 * operations over a list of {@link Sale} objects.
 *
 * <p>This class demonstrates functional-style data processing using
 * Java Streams, Lambdas, and Collectors. It supports a variety of
 * analytical tasks including revenue calculations, product- and
 * region-based aggregations, trend analysis, summaries, and filters.</p>
 *
 * <p>All methods are pure functions — they do not modify input lists and
 * return new immutable results.</p>
 */
public class SalesAnalyzer {

    /**
     * Calculates the total revenue across all sales.
     *
     * @param sales list of Sale records
     * @return sum of sale.revenue() for all records
     */
    public double getTotalRevenue(List<Sale> sales) {
        return sales.stream()
                .mapToDouble(Sale::revenue)   // compute revenue = unitPrice * quantity
                .sum();                       // sum all revenue values
    }

    /**
     * Calculates the total number of units sold across all sales.
     */
    public long getTotalUnits(List<Sale> sales) {
        return sales.stream()
                .mapToLong(Sale::getQuantity) // extract quantity
                .sum();                       // sum all units
    }

    /**
     * Groups sales by region and computes total revenue for each region.
     *
     * @return Map<Region, TotalRevenue>
     */
    public Map<String, Double> getRevenueByRegion(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getRegion,               // grouping key
                Collectors.summingDouble(Sale::revenue) // aggregation
        ));
    }

    /**
     * Computes total revenue for each product.
     */
    public Map<String, Double> getRevenueByProduct(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getProductId,
                Collectors.summingDouble(Sale::revenue)
        ));
    }

    /**
     * Computes total units sold for each product.
     */
    public Map<String, Long> getUnitsByProduct(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getProductId,
                Collectors.summingLong(Sale::getQuantity)
        ));
    }

    /**
     * Groups sales by YearMonth and sums revenue for each month.
     */
    public Map<YearMonth, Double> getMonthlyRevenue(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                sale -> YearMonth.from(sale.getDate()), // convert LocalDate → YearMonth
                Collectors.summingDouble(Sale::revenue)
        ));
    }

    /**
     * Computes revenue for each individual date.
     */
    public Map<LocalDate, Double> getDailyRevenue(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getDate,
                Collectors.summingDouble(Sale::revenue)
        ));
    }

    /**
     * Aggregates revenue based on product category.
     */
    public Map<String, Double> getRevenueByCategory(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getCategory,
                Collectors.summingDouble(Sale::revenue)
        ));
    }

    /**
     * Computes the average revenue contributed by each sale.
     */
    public double getAverageRevenuePerSale(List<Sale> sales) {
        return sales.stream()
                .mapToDouble(Sale::revenue)
                .average()
                .orElse(0);  // default value for empty list
    }

    /**
     * Finds the sale entry with the maximum revenue.
     */
    public Optional<Sale> getHighestSale(List<Sale> sales) {
        return sales.stream()
                .max(Comparator.comparingDouble(Sale::revenue));
    }

    /**
     * Finds the sale entry with the minimum revenue.
     */
    public Optional<Sale> getLowestSale(List<Sale> sales) {
        return sales.stream()
                .min(Comparator.comparingDouble(Sale::revenue));
    }

    /**
     * Returns the top N products ranked by total revenue.
     *
     * @param n number of top products to return
     */
    public List<Map.Entry<String, Double>> getTopNProductsByRevenue(List<Sale> sales, int n) {
        return getRevenueByProduct(sales).entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed()) // high → low revenue
                .limit(n)
                .collect(Collectors.toList());
    }


    /**
     * Returns the top N regions ranked by revenue.
     */
    public List<Map.Entry<String, Double>> getTopRegions(List<Sale> sales, int n) {
        return getRevenueByRegion(sales).entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .collect(Collectors.toList());
    }


    /**
     * Aggregates total revenue generated by each salesperson.
     */
    public Map<String, Double> getRevenueBySalesperson(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getSalesperson,
                Collectors.summingDouble(Sale::revenue)
        ));
    }


    /**
     * Filters and returns sales whose revenue exceeds a threshold.
     */
    public List<Sale> getHighValueSales(List<Sale> sales, double threshold) {
        return sales.stream()
                .filter(s -> s.revenue() >= threshold)
                .toList();  // Java 16 immutable list
    }

    /**
     * Counts number of sale entries in each region.
     */
    public Map<String, Long> getSaleCountByRegion(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getRegion,
                Collectors.counting()
        ));
    }

    /**
     * Produces a summary for each category containing:
     * <ul>
     *     <li>Total revenue</li>
     *     <li>Total units sold</li>
     * </ul>
     */
    public Map<String, Map<String, Number>> getCategorySummary(List<Sale> sales) {

        Map<String, Double> revenue = getRevenueByCategory(sales);

        Map<String, Long> units = sales.stream().collect(Collectors.groupingBy(
                Sale::getCategory,
                Collectors.summingLong(Sale::getQuantity)
        ));

        Map<String, Map<String, Number>> result = new HashMap<>();

        // Combine revenue + units into a nested map
        revenue.forEach((cat, rev) -> {
            Map<String, Number> summary = new HashMap<>();
            summary.put("revenue", rev);
            summary.put("units", units.get(cat));
            result.put(cat, summary);
        });

        return result;
    }

    /**
     * Produces a summary for each product containing:
     * <ul>
     *     <li>Total revenue</li>
     *     <li>Total units sold</li>
     *     <li>Average price = revenue / units</li>
     * </ul>
     */
    public Map<String, Map<String, Number>> getProductSummary(List<Sale> sales) {

        Map<String, Double> revenue = getRevenueByProduct(sales);
        Map<String, Long> units = getUnitsByProduct(sales);

        Map<String, Map<String, Number>> result = new HashMap<>();

        revenue.forEach((productId, rev) -> {
            Map<String, Number> summary = new HashMap<>();
            summary.put("revenue", rev);
            summary.put("units", units.get(productId));
            summary.put("avg_price", rev / units.get(productId));
            result.put(productId, summary);
        });

        return result;
    }

    /**
     * Returns count of sales per month.
     */
    public Map<YearMonth, Long> getMonthlySaleCount(List<Sale> sales) {
        return sales.stream().collect(Collectors.groupingBy(
                sale -> YearMonth.from(sale.getDate()),
                Collectors.counting()
        ));
    }


    /**
     * Generic method that groups any type of data based on a classifier function.
     *
     * @param data       list of items to group
     * @param classifier function to compute grouping key
     * @return Map<Key, List<Items>>
     */
    public <T, R> Map<R, List<T>> groupBy(List<T> data, Function<T, R> classifier) {
        return data.stream().collect(Collectors.groupingBy(classifier));
    }
}
