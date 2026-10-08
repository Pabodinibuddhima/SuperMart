# SuperMart - Sales & Inventory Management System

**Enterprise Application Development (EAD) Coursework | Java Swing • MySQL • JasperReports**

SuperMart is a Java desktop application developed to support retail sales, inventory control, and business management operations.

## Download the Application

### [Download SuperMart v1.0.0 - Executable JAR & Release Package](https://github.com/Pabodinibuddhima/SuperMart/releases/tag/v1.0.0)

The GitHub release provides:

- **`SuperMart.jar`** - the executable Java application with required dependencies.
- **`SuperMart-v1.0.0.zip`** - the complete release package, containing the JAR, database scripts, configuration template, and launcher scripts.

**System requirements:** Java 21 and MySQL Server.

**Important:** The application requires a configured MySQL database before it can be used. Follow the installation instructions below.

---

## Academic Information

| Field | Details |
|---|---|
| Project | SuperMart - Sales & Inventory Management System |
| Student | Samaranayake P.B |
| Student ID | CODCSD253F-008 |
| Institute | National Institute of Business Management (NIBM) |
| Programme | Diploma in Computer System Design |
| Batch | DCSD25.3F |
| Module | Enterprise Application Development (EAD) |
| Project Type | Individual Coursework |
| Sector | Sales and Retail Management |
| Final Deliverable | Executable JAR |

## 1. Project Overview

SuperMart is designed to simplify the daily operations of a retail business through a centralized desktop interface.

The application integrates sales processing, product management, inventory tracking, customer and supplier management, employee records, returns and refunds, and financial reporting.

It uses Java Swing for the user interface, MySQL for persistent data storage, JDBC for database connectivity, and JasperReports for reporting and PDF export.

The project demonstrates object-oriented programming, layered application architecture, database operations, transaction processing, validation, exception handling, and standalone Java application deployment.

## 2. Application Features

### Dashboard
- Today's sales and transaction statistics
- Active product count
- Low-stock and out-of-stock alerts
- Recent sales activity
- Quick access to key management functions

### Product Management
- Add and update product information
- Manage categories, suppliers, prices, and stock information
- Activate or deactivate products
- Search, filter, and sort product records

### Inventory Management
- Record stock entries and adjustments
- Monitor available stock
- View stock transaction history
- Automatically update inventory following sales and returns
- Identify products requiring restocking

### Customer, Supplier, and Employee Management
- Maintain customer records
- Manage supplier information
- Maintain employee information
- Update and manage existing records

### Sales and Payment Processing
- Create sales transactions
- Add products and quantities to a sales cart
- Calculate totals and discounts
- Process supported payment methods
- Calculate cash change where applicable
- View sales history and transaction details
- Automatically reduce stock after successful sales

### Returns and Refunds
- Process full and partial product returns
- Validate return quantities
- Prevent invalid or excessive returns
- Restore stock for returned products
- Record refund information and update transaction status

### Reporting
- Generate sales performance reports using JasperReports
- Retrieve information from multiple related database tables
- Display sales and product transaction details
- Calculate gross sales, discounts, refunds, net sales, cost of goods sold, and gross profit
- Export generated reports to PDF

### Application Settings
- Manage store information
- Configure database connection settings
- Test database connectivity
- View application and system information

## 3. Technologies and Tools

| Technology | Purpose |
|---|---|
| Java 21 | Core application development |
| Java Swing | Desktop graphical user interface |
| MySQL | Relational database management |
| JDBC | Database connectivity |
| Apache Maven | Dependency and build management |
| JasperReports 6.21.5 | Reporting and PDF generation |
| Maven Shade Plugin | Packaging application dependencies into an executable JAR |
| Git and GitHub | Version control and source code distribution |
| Apache NetBeans | Development environment |

## 4. Application Architecture

The project follows a layered, object-oriented structure to separate user interface components, business logic, database operations, and reporting.

| Package | Responsibility |
|---|---|
| `model` | Application entities and data models |
| `view` | Java Swing interface components |
| `dao` | Database access and CRUD operations |
| `service` | Business rules and transaction processing |
| `exception` | Custom application exceptions |
| `report` | JasperReports integration |
| `util` | Database connections, configuration, and shared utilities |

This separation supports maintainability, reuse, and structured error handling.

## 5. Database Design

SuperMart uses a MySQL database named `supermart_db`.

The schema contains 11 related tables:

1. `categories`
2. `suppliers`
3. `products`
4. `stock_transactions`
5. `customers`
6. `employees`
7. `sales`
8. `sale_items`
9. `payments`
10. `returns`
11. `return_items`

The database uses relational constraints and foreign keys to maintain relationships between records.

SQL scripts are provided for database creation and demonstration data.

## 6. Project Structure

```text
SuperMart/
├── src/
│   └── main/
│       ├── java/com/supermart/
│       │   ├── dao/
│       │   ├── exception/
│       │   ├── model/
│       │   ├── report/
│       │   ├── service/
│       │   ├── util/
│       │   └── view/
│       └── resources/
├── database/
│   ├── schema.sql
│   └── sample_data.sql
├── config/
│   └── db.properties.example
├── pom.xml
├── run-linux.sh
├── run-windows.bat
└── README.md
```

Generated JAR files and local database credentials are excluded from normal Git tracking.

The executable application is distributed separately through GitHub Releases.

## 7. Installation Requirements

To run the packaged application, the target computer requires:

- Java 21 runtime or JDK
- MySQL Server
- A configured `supermart_db` database
- The SuperMart release package

**NetBeans, Maven, and the Java compiler are not required to run the executable JAR.**

An internet connection is not required for normal operation when the application connects to a local MySQL server.

## 8. Download and Extract

Download the application from:

**[SuperMart v1.0.0 Release](https://github.com/Pabodinibuddhima/SuperMart/releases/tag/v1.0.0)**

Extract `SuperMart-v1.0.0.zip` into a suitable directory.

Expected release structure:

```text
SuperMart-Release/
├── SuperMart.jar
├── config/
│   └── db.properties.example
├── database/
│   ├── schema.sql
│   └── sample_data.sql
├── run-linux.sh
└── run-windows.bat
```

Keep the application, configuration folder, and database scripts together.

## 9. Database Setup

### Step 1 - Install MySQL

Install MySQL Server and ensure the MySQL service is running.

### Step 2 - Create the Database Tables

Open a terminal or Command Prompt inside the extracted `SuperMart-Release` directory.

Run:

```bash
mysql -u root -p < database/schema.sql
```

Enter the MySQL password when prompted.

The script creates the `supermart_db` database and its required tables.

### Step 3 - Import Sample Data (Optional)

For demonstration purposes, sample records can be imported into a fresh database:

```bash
mysql -u root -p supermart_db < database/sample_data.sql
```

Avoid importing sample data repeatedly into an existing database.

## 10. Database Configuration

SuperMart uses an external configuration file, allowing database settings to be changed without rebuilding the JAR.

Copy the configuration template.

**Windows CMD:**

```bat
copy config\db.properties.example config\db.properties
```

**Linux:**

```bash
cp config/db.properties.example config/db.properties
```

Open `config/db.properties` in a text editor.

Example configuration:

```properties
db.url=jdbc:mysql://localhost:3306/supermart_db
db.user=root
db.password=YOUR_MYSQL_PASSWORD
```

Replace the example credentials with those of your configured MySQL account.

The application reads its database configuration from the `config` directory.

**Security:** Actual database credentials are excluded from the public repository and release package.

## 11. Running the Application

### Windows 10 / Windows 11

Open Command Prompt inside the extracted `SuperMart-Release` directory.

Run:

```bat
run-windows.bat
```

Alternatively:

```bat
java -jar SuperMart.jar
```

### Linux

Open a terminal inside the extracted `SuperMart-Release` directory.

Run:

```bash
chmod +x run-linux.sh
./run-linux.sh
```

Alternatively:

```bash
java -jar SuperMart.jar
```

The supplied launcher scripts are intended to start the application from its release directory so the external configuration file can be located.

## 12. First-Time Usage

After launching the application:

1. Open **Settings** and verify the database connection.
2. Use **Test Connection** to confirm successful connectivity.
3. Configure store information if necessary.
4. Open **Dashboard** to review sales and inventory statistics.
5. Add or manage products, customers, suppliers, and employees.
6. Record inventory entries where necessary.
7. Open **Sales** to create a transaction.
8. Use **Sales History** to review completed transactions.
9. Process returns or refunds where applicable.
10. Open **Reports** to generate or export sales performance reports.

## 13. Sales Reporting

The reporting module uses JasperReports and relational database queries to generate sales performance information.

Available report functionality includes:

- Transaction-level sales details
- Product and category information
- Customer and employee associations
- Gross sales and discount totals
- Refund calculations
- Net sales
- Cost of goods sold
- Gross profit

Reports can be viewed within the application and exported as PDF files.

## 14. Build from Source

Developers require JDK 21, Maven, and MySQL Server.

Clone the repository:

```bash
git clone https://github.com/Pabodinibuddhima/SuperMart.git
cd SuperMart
```

Configure the database as described above.

Build the application:

```bash
mvn clean package
```

The Maven Shade Plugin produces the executable JAR at:

```text
target/SuperMart-1.0-SNAPSHOT-all.jar
```

Run it from the project root:

```bash
java -jar target/SuperMart-1.0-SNAPSHOT-all.jar
```

The JAR contains the required Java libraries, including the MySQL JDBC driver and JasperReports dependencies.

## 15. Troubleshooting

### Java Not Found

Check the installed Java version:

```bash
java -version
```

Install Java 21 if Java is missing or an incompatible version is installed.

### Database Connection Failed

Verify that:

- MySQL Server is running.
- The `supermart_db` database exists.
- The database tables have been imported.
- The configured username and password are correct.
- The database host and port are correct.
- The `config/db.properties` file exists.

### Access Denied

Verify the database user's credentials and permissions.

### Missing Database Tables

Import `database/schema.sql` before using the application.

### Reports Cannot Be Generated

Check the database connection and verify that the packaged application includes its reporting dependencies.

### Application Does Not Start

Run the application from a terminal or Command Prompt to view any error messages:

```bash
java -jar SuperMart.jar
```

Ensure the command is executed from the directory containing the JAR.

## 16. Testing and Verification

The application has been tested on Fedora Linux using the standalone executable JAR.

Verified workflows include:

- Database connection and settings
- Dashboard and management interfaces
- Sales transaction processing
- Automatic inventory deduction after a sale
- Sales and stock transaction history
- Returns and refunds
- JasperReports report generation
- PDF export
- Launching the application from the extracted release directory

These tests demonstrate the operation of the packaged Java application with its external database configuration.

## 17. Deployment Status

| Deliverable | Status |
|---|---|
| Java Swing application | Completed |
| MySQL integration | Completed |
| Sales and inventory management | Completed |
| Returns and refunds | Completed |
| JasperReports and PDF export | Completed |
| Maven build | Completed |
| Standalone executable JAR | Completed and tested on Linux |
| External database configuration | Completed |
| Windows and Linux launcher scripts | Included |
| Native Windows EXE | Optional - not included in v1.0.0 |

The primary coursework deliverable is the executable JAR.

The native Windows executable is considered an optional enhancement and is not required to run the Java application.

## 18. Academic Purpose

SuperMart was developed as an individual academic coursework project for the Enterprise Application Development module at the National Institute of Business Management (NIBM), under the Diploma in Computer System Design programme.

The application demonstrates practical desktop software development, relational database integration, business transaction processing, report generation, and Java application deployment.

**Developed by:** Samaranayake P.B  
**Student ID:** CODCSD253F-008  
**Batch:** DCSD25.3F
