# Sales Analysis Using Java Streams & Functional Programming


## 📌 Project Overview

This project demonstrates **functional programming**, **Java Streams**, **lambda expressions**, **CSV parsing**, and **data aggregation**.  
It analyzes sales data from a CSV file and generates a detailed analytical report.

The project also includes **full JUnit  test coverage**, covering:
- `SalesAnalyzer`
- `Sale`
- `CsvReader`
- `App` (smoke-tested)

---

## 📄 CSV Format (Expected Schema)

The CSV file **must** follow this exact structure:

| Column       | Description                         |
|--------------|-------------------------------------|
| sale_id      | Unique sale identifier              |
| date         | Date in `yyyy-MM-dd` format         |
| product_id   | Product code                       |
| product_name | Name of product                    |
| category     | Product category                   |
| unit_price   | Price per unit                     |
| quantity     | Quantity sold                      |
| region       | Sales region                       |
| salesperson  | Name of salesperson                |

### Example:

sale_id,date,product_id,product_name,category,unit_price,quantity,region,salesperson
1,2024-01-01,P101,Keyboard,Electronics,1200,3,APAC,Rohan

---

## ▶️ How to Run

### 1. Build the project
```bash
mvn clean install
```
---

## 🧪 Running Tests
Run all JUnit tests using:

```bash
mvn test
```
## Coverage includes:

CSV parsing

All 20+ analytical methods

App execution test (smoke test)

## 📊 Analytical Outputs
The application prints:

Total Revenue

Total Units Sold

Revenue by Region

Revenue by Product

Revenue by Category

Units Sold by Product

Monthly Revenue

Daily Revenue

Monthly Sale Count

Revenue by Salesperson

High-Value Sales

Top N Products

Top N Regions

Category Summary (units + revenue)

Product Summary (units + revenue + avg price)

Each of these uses functional programming + Streams.