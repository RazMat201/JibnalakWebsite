# Jebnalak E-commerce Website – Automated UI Testing

## Project Overview

This project is a Selenium WebDriver-based automated test suite for the **Jebnalak** e-commerce platform. It simulates real user interactions to verify that key functionalities across the site work as expected. The test cases are written in Java with TestNG, and the project uses MySQL for dynamic test data handling.

The tests cover homepage loading, product search (using database input), filtering, cart functionality, checkout validation, language switching, and policy page accessibility.

---

## Technologies Used

- Java
- Selenium WebDriver
- TestNG
- MySQL (via JDBC)
- ChromeDriver
- Eclipse IDE or IntelliJ

---

## Getting Started

### 1. Clone the Repository

Use Git to clone the repository to your local machine.

### 2. Prerequisites

- Java Development Kit (JDK)
- MySQL Server installed locally
- Chrome browser
- ChromeDriver (same version as your Chrome browser)
- An IDE such as Eclipse or IntelliJ
- MySQL Workbench (optional)

---

## Database Setup

To prepare the database for dynamic search testing:

1. Open MySQL Workbench.
2. Create a new database named `jibnalakdatabase`.
3. Within that database, create a table named `products` with the following structure:

   - `product_name`: Text field to store product names (e.g., "coffee").
   - `category_name`: Text field for category names (e.g., "Coffee & Tea").
   - `brand_name`: Text field for the brand (e.g., "Alameed").

   All fields can be of type VARCHAR(255).

   Example insert during test run:  
   `'coffee'`, `'Coffee & Tea'`, `'Alameed'`

This table is accessed and modified dynamically during test execution.

---

## How to Run the Tests

1. Make sure ChromeDriver is in your system path or specify the path manually in the code.
2. In the Java code (`MainTestCases.java`), update the line that connects to the database to reflect your local credentials. For example:

   - JDBC URL: `jdbc:mysql://localhost:3306/jibnalakdatabase`
   - Username: `root`
   - Password: your MySQL password

3. Import the project into your IDE.
4. Right-click the test class file and select "Run as TestNG Test" (if using Eclipse).
5. (Optional) You may uncomment the `@AfterTest` method to close the browser automatically after tests complete.

---

## Test Scenarios Implemented

### TC_JEB_001 – Homepage Accessibility
- Checks if the page fully loads and displays top menu and featured products.

### TC_JEB_002 – Product Search Functionality
- Uses product data from MySQL database to perform a search and validate results.

### TC_JEB_003 – Viewing Product Details
- Opens a random product and ensures all its details are visible.

### TC_JEB_004 – Privacy Policy Accessibility
- Navigates to the Policies page and confirms the presence of privacy, delivery, or refund policy text.

### TC_JEB_005 – Filtering Search Results
- Selects a category and applies a "Price Low to High" sort.
- Verifies that items are displayed in correct price order.

### TC_JEB_006 – Adding Products to Cart
- Adds products from different categories and validates cart update.

### TC_JEB_007 – Viewing & Modifying the Cart
- Increases product quantities and removes one item.
- Checks if subtotal reflects the changes.

### TC_JEB_008 – Proceeding to Checkout
- Navigates to the checkout page and ensures that all required input fields are present.

### TC_JEB_009 – Language Switch Functionality
- Switches between Arabic and English.
- Validates page URL and checks if content is in the correct language.

### TC_JEB_010 – Logo and Info Visibility
- Verifies that the site logo and footer contact information are properly displayed.

---

## Notes

- All assertions are handled via TestNG.
- Database is used dynamically for inserting, retrieving, and deleting product search inputs.
- Some waits are implemented with `Thread.sleep()`, which can be optimized using explicit waits.
- Use TestNG XML configuration if you want more control over test execution order.

---

## License

This project is for educational and QA training purposes. You are free to modify or extend it for your own use.

