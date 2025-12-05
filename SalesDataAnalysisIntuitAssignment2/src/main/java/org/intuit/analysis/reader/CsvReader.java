package org.intuit.analysis.reader;

import org.intuit.analysis.model.Sale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class responsible for reading a CSV file and converting
 * each row into a {@link Sale} object.
 *
 * <p>This class uses Java NIO and Streams for efficient and functional-style
 * file processing. It automatically:
 * <ul>
 *     <li>Reads the CSV file line-by-line</li>
 *     <li>Skips the header row</li>
 *     <li>Trims whitespace</li>
 *     <li>Ignores empty lines</li>
 *     <li>Delegates parsing logic to {@code Sale.fromCsv}</li>
 * </ul>
 * </p>
 */
public class CsvReader {

    /**
     * Reads a CSV file from the given file path and converts all rows into Sale objects.
     *
     * <p>Steps performed:
     * <ol>
     *     <li>Open a stream of file lines using {@link Files#lines}</li>
     *     <li>Skip the first line (header)</li>
     *     <li>Remove empty or whitespace-only lines</li>
     *     <li>Trim each line to ensure clean parsing</li>
     *     <li>Convert each line into a Sale object using {@link Sale#fromCsv}</li>
     *     <li>Collect all Sale objects into a List</li>
     * </ol>
     *
     * @param filePath Fully qualified path to the CSV file
     * @return List of Sale objects parsed from the CSV
     * @throws IOException if the file cannot be read
     */
    public static List<Sale> readSales(String filePath) throws IOException {

        try (Stream<String> lines = Files.lines(Path.of(filePath))) {

            return lines
                    .skip(1)                               // Skip the CSV header line (column names)
                    .map(String::trim)                       // Ignore blank/whitespace-only lines
                    .filter(trim -> !trim.isEmpty())  // Trim unnecessary spaces around each line
                    .map(Sale::fromCsv)                     // Convert each line → Sale object
                    .collect(Collectors.toList());          // Collect results into a List
        }
    }
}
