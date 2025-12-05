package org.intuit.analysis;

import org.intuit.analysis.model.Sale;
import org.intuit.analysis.reader.CsvReader;
import org.intuit.analysis.service.SalesAnalyzer;

import java.util.List;

/**
 * Entry point for the Sales Analysis application.
 *
 * <p>This class orchestrates the overall workflow:
 * <ul>
 *     <li>Loads the CSV file using {@link CsvReader}</li>
 *     <li>Runs 20+ analytical operations using {@link SalesAnalyzer}</li>
 *     <li>Prints a structured sales analysis report to the console</li>
 * </ul>
 *
 * <p>The application demonstrates functional programming, stream processing,
 * aggregation logic, and clean backend design.</p>
 */
public class App {

    /**
     * Main method that runs the complete sales analysis pipeline.
     *
     * @param args Accepts an optional file path. If no argument is passed,
     *             the default CSV file in resources is used.
     */
    public static void main(String[] args) throws Exception {

        String filePath = (args.length > 0)
                ? args[0]   // user-supplied file path
                : "src/main/resources/sales.csv"; // default file

        List<Sale> sales = CsvReader.readSales(filePath);

        // Create analyzer instance
        SalesAnalyzer analyzer = new SalesAnalyzer();

        System.out.println("\n============== SALES ANALYSIS REPORT ==============\n");

        System.out.println("Total Revenue: " + analyzer.getTotalRevenue(sales));

        System.out.println("Total Units Sold: " + analyzer.getTotalUnits(sales));

        analyzer.getHighestSale(sales).ifPresent(s ->
                System.out.println("Highest Sale Revenue: " + s.revenue()));

        analyzer.getLowestSale(sales).ifPresent(s ->
                System.out.println("Lowest Sale Revenue: " + s.revenue()));

        System.out.println("\n------------ Revenue By Region ------------");
        analyzer.getRevenueByRegion(sales)
                .forEach((region, revenue) ->
                        System.out.println(region + " : " + revenue));

        System.out.println("\n------------ Revenue By Product ------------");
        analyzer.getRevenueByProduct(sales)
                .forEach((product, rev) ->
                        System.out.println(product + " : " + rev));

        System.out.println("\n------------ Units Sold By Product ------------");
        analyzer.getUnitsByProduct(sales)
                .forEach((product, units) ->
                        System.out.println(product + " : " + units));

        System.out.println("\n------------ Revenue By Category ------------");
        analyzer.getRevenueByCategory(sales)
                .forEach((category, rev) ->
                        System.out.println(category + " : " + rev));

        System.out.println("\n------------ Category Summary (Revenue + Units) ------------");
        analyzer.getCategorySummary(sales)
                .forEach((cat, summary) ->
                        System.out.println(cat + " => " + summary));

        System.out.println("\n------------ Product Summary (Revenue + Units + Avg Price) ------------");
        analyzer.getProductSummary(sales)
                .forEach((prod, summary) ->
                        System.out.println(prod + " => " + summary));

        System.out.println("\n------------ Monthly Revenue ------------");
        analyzer.getMonthlyRevenue(sales)
                .forEach((month, revenue) ->
                        System.out.println(month + " : " + revenue));

        System.out.println("\n------------ Monthly Sale Count ------------");
        analyzer.getMonthlySaleCount(sales)
                .forEach((month, count) ->
                        System.out.println(month + " : " + count));

        System.out.println("\n------------ Daily Revenue ------------");
        analyzer.getDailyRevenue(sales)
                .forEach((date, revenue) ->
                        System.out.println(date + " : " + revenue));

        System.out.println("\n------------ Revenue By Salesperson ------------");
        analyzer.getRevenueBySalesperson(sales)
                .forEach((sp, revenue) ->
                        System.out.println(sp + " : " + revenue));

        System.out.println("\n------------ High Value Sales (>= 5000) ------------");
        analyzer.getHighValueSales(sales, 5000)
                .forEach(s ->
                        System.out.println("Sale Revenue: " + s.revenue()));

        System.out.println("\n------------ Top 3 Products By Revenue ------------");
        analyzer.getTopNProductsByRevenue(sales, 3)
                .forEach(entry ->
                        System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\n------------ Top 2 Regions By Revenue ------------");
        analyzer.getTopRegions(sales, 2)
                .forEach(entry ->
                        System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\n------------ Sale Count By Region ------------");
        analyzer.getSaleCountByRegion(sales)
                .forEach((region, count) ->
                        System.out.println(region + " : " + count));

        System.out.println("\nAnalysis Completed Successfully.");
    }
}
