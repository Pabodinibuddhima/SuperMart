# SuperMart — Sales & Inventory Management System

**Enterprise Application Development | Java Desktop Application**

SuperMart is a desktop-based Sales and Inventory Management System developed using Java Swing and MySQL. It provides a centralized interface for managing retail business operations, including sales, products, inventory, customers, suppliers, employees, returns, and financial reporting.

## 1. Features

### Dashboard
- Today's net sales and transaction count
- Active product statistics
- Recent sales transactions
- Low-stock and out-of-stock alerts

### Product and Inventory Management
- Add, edit, activate, and deactivate products
- Manage product prices, barcodes, categories, and suppliers
- Record stock entries and adjustments
- Track inventory transactions
- Search, filter, and sort records

### Customer, Supplier, and Employee Management
- Manage customer information
- Maintain supplier records
- Manage employee information and roles
- Activate or deactivate records

### Sales and Payments
- Create sales invoices
- Select customers and cashiers
- Add products to a shopping cart
- Apply discounts
- Process cash and card payments
- Calculate payment totals and cash change
- View sales history and transaction details

### Returns and Refunds
- Process full and partial returns
- Validate return quantities
- Prevent returns exceeding quantities available for refund
- Restore returned stock
- Update sale refund status

### Reporting
- Generate sales performance reports using JasperReports
- View transaction and product sales information
- Calculate gross sales, discounts, refunds, net sales, cost of goods sold, and gross profit
- Export reports to PDF

### Application Settings
- Configure store information
- Configure database connection details
- Test database connectivity
- View system information

## 2. Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| Java Swing | Desktop graphical interface |
| MySQL | Relational database |
| JDBC | Database connectivity |
| Maven | Build and dependency management |
| JasperReports 6.21.5 | Reporting and PDF export |
| Maven Shade Plugin | Standalone JAR packaging |
| Git and GitHub | Version control |

## 3. Project Structure

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

## 4. Requirements

To run the standalone JAR, the target computer needs:

- Java Runtime version 21 or newer
- MySQL Server compatible with the supplied database schema
- A configured SuperMart database
- The SuperMart release package

**Maven, NetBeans, and the JDK compiler are not required to run the packaged JAR.**

An internet connection is not required for ordinary local application operation once the software and database are installed.

## 5. Download the Application

Download the latest SuperMart release ZIP from the repository's **Releases** section.

Extract the ZIP into a folder.

The release package contains:

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

## 6. Database Installation

### Step 1 — Install MySQL

Install MySQL Server on the target computer and ensure the database service is running.

### Step 2 — Import the Database Schema

Open a terminal or Command Prompt in the extracted `SuperMart-Release` folder.

Run:

```bash
mysql -u root -p < database/schema.sql
```

Enter your MySQL password when prompted.

The schema creates the `supermart_db` database and its required tables.

### Step 3 — Optional Sample Data

For a new demonstration database, import the sample records:

```bash
mysql -u root -p supermart_db < database/sample_data.sql
```

Use sample data only when appropriate for a fresh installation.

## 7. Database Configuration

Copy the provided configuration template.

**Windows CMD:**

```bat
copy config\db.properties.example config\db.properties
```

**Linux:**

```bash
cp config/db.properties.example config/db.properties
```

Open `config/db.properties` and configure the database connection.

Example:

```properties
db.url=jdbc:mysql://localhost:3306/supermart_db
db.user=root
db.password=YOUR_MYSQL_PASSWORD
```

Replace `YOUR_MYSQL_PASSWORD` with the correct password.

The configuration file must remain in the `config` directory.

**Security:** Do not upload real database passwords or personal credentials to GitHub. The repository includes only a configuration template.

## 8. Running SuperMart

### Windows 10 / Windows 11

Open Command Prompt in the extracted release folder.

Run:

```bat
run-windows.bat
```

Alternatively:

```bat
java -jar SuperMart.jar
```

### Linux

Open a terminal in the extracted release folder.

Run:

```bash
chmod +x run-linux.sh
./run-linux.sh
```

Alternatively:

```bash
java -jar SuperMart.jar
```

The provided launch scripts ensure the application starts from its release directory so it can locate `config/db.properties`.

## 9. First-Time Application Setup

After launching SuperMart:

1. Open **Settings**.
2. Review the database connection details.
3. Click **Test Connection** to verify MySQL connectivity.
4. Configure store information if required.
5. Open **Dashboard** to review the current business overview.
6. Use **Products**, **Inventory**, **Customers**, **Suppliers**, and **Employees** to manage records.
7. Open **Sales** to process a transaction.
8. Open **Reports** to view or export sales performance information.

## 10. Generating Reports

1. Open **Reports** from the sidebar.
2. Select **View Report** to open the JasperReports viewer.
3. Select **Export PDF** to save the report as a PDF file.

Reports use data stored in the configured MySQL database.

## 11. Building from Source Code

Developers need:

- JDK 21
- Apache Maven
- MySQL Server

Clone the repository:

```bash
git clone https://github.com/Pabodinibuddhima/SuperMart.git
cd SuperMart
```

Configure the database using the instructions above.

Build the application:

```bash
mvn clean package
```

The standalone JAR is generated at:

```text
target/SuperMart-1.0-SNAPSHOT-all.jar
```

Launch the JAR from the project directory:

```bash
java -jar target/SuperMart-1.0-SNAPSHOT-all.jar
```

## 12. Troubleshooting

**Java is not recognized**

Install Java 21 or newer and ensure `java` is available in the system PATH.

Check with:

```bash
java -version
```

**Database connection failed**

Verify that:

- MySQL Server is running.
- The `supermart_db` database exists.
- The username and password are correct.
- The configured host and port are correct.
- `config/db.properties` exists.

**Access denied for MySQL user**

Check the MySQL username, password, and database permissions.

**Database tables are missing**

Import `database/schema.sql` before using the application.

**Report generation failed**

Check database connectivity and ensure the application was built using the supplied Maven configuration and dependencies.

## 13. Architecture and Design

The project separates responsibilities across:

- **Model:** Business entities and application data
- **View:** Java Swing interfaces
- **DAO:** Database operations
- **Service:** Business logic and transaction processing
- **Exception:** Custom application exceptions
- **Report:** JasperReports integration
- **Util:** Database configuration and shared utilities

The application applies object-oriented programming principles and structured database access.

## 14. Deployment Status

| Deliverable | Status |
|---|---|
| Java Swing desktop application | Completed |
| MySQL database integration | Completed |
| Sales and inventory functionality | Completed |
| Returns and refunds | Completed |
| JasperReports and PDF export | Completed |
| Standalone executable JAR | Completed |
| Windows and Linux launcher scripts | Completed |
| Windows native EXE | Optional — not yet packaged |

## 15. Academic Information

**Project:** SuperMart — Sales & Inventory Management System

**Module:** Enterprise Application Development

**Sector:** Sales and Retail Management

**Application Type:** Java Desktop Application

**Purpose:** Academic coursework and demonstration.
