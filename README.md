# Sales Software

This project provides functionality to calculate various metrics related to sales, discounts, and customer orders. It includes features to calculate total sales before and after discounts, the total amount lost via customer discount codes, and the average discount per customer as a percentage.

## Features

- **Total Sales Calculation**: Calculates the total sales amount before and after applying discounts.
- **Discount Calculation**: Calculates the total amount lost via customer discount codes.
- **Average Discount Calculation**: Calculates the average discount per customer as a percentage.

## Requirements

- Java Development Kit (JDK) 8 or higher
- Apache Maven (for building the project)
- JSON simple library (included as a dependency)

## Installation and Usage

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/sales-software.git
    ```

2. Navigate to the project directory:

    ```bash
    cd sales-software
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```

4. Run the application:

    ```bash
    java -jar target/sales-software.jar
    ```

5. View the output in the console. The application will display total sales before discount, total sales after discount, total amount lost via customer discount codes, and average discount per customer as a percentage.

## Configuration

- Discount codes, order details, and product prices are loaded from JSON files located in the `src/main/resources/part1` directory. You can modify these files as needed.

## Logging

- The application uses SLF4J for logging. Logging configuration can be customized in the `logback.xml` file.
