# Finance Dashboard Backend

A cleanly structured Spring Boot backend for a finance dashboard system featuring user role management, financial record management, dashboard summaries, and role-based access control (RBAC).

## Setup Process

### Prerequisites
- **Java 17** or higher
- **Maven** (included via `./mvnw` wrapper)
- A tool like **Postman** or a web browser to access Swagger UI.

### Running the Application Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/Mayur192004/finance-backend-springboot.git
   cd finance-backend-springboot
   ```
2. Start the Spring Boot application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The server will start on port `8080`. You can access the **Swagger UI** for testing at:
   `http://localhost:8080/swagger-ui.html`

4. Access the **H2 Database Console** (optional):
   - **URL:** `http://localhost:8080/h2-console`
   - **JDBC URL:** `jdbc:h2:mem:financedb`
   - **Username:** `sa`
   - **Password:** `password`

## API Explanation

The APIs are logically organized into controllers and documented exhaustively by the **Swagger UI**. Key domains include:

### 1. Auth (`/api/auth`)
- `POST /api/auth/register`: Register a new user with a specific role (`ADMIN`, `ANALYST`, `VIEWER`).
- `POST /api/auth/login`: Authenticate an existing user and receive a secure JWT token.

### 2. Users (`/api/users`) - *Admin Only*
- `GET /api/users`: List all users registered in the system.
- `PUT /api/users/{id}/role`: Update an existing user's role.
- `PUT /api/users/{id}/status`: Enable or disable a user account.

### 3. Financial Records (`/api/records`)
- `POST /api/records`: Create a new `INCOME` or `EXPENSE` record. Includes amount, category, date, and notes. (*Admin Only*)
- `GET /api/records`: Retrieve paginated records. Viewers, Analysts, and Admins can dynamically filter results by `type`, `category`, `startDate`, and `endDate`.
- `PUT /api/records/{id}` and `DELETE /api/records/{id}`: Update or Soft-Delete existing records. (*Admin Only*)

### 4. Dashboard (`/api/dashboard`)
- `GET /api/dashboard/summary`: Retrieve an aggregated view of the active dataset. Returns Total Income, Total Expenses, Net Balance, Category-wise breakdown amounts, and the 5 most recent activities. (*Admin & Analyst Only*)

## Assumptions Made

1. **Role Strategy**: It is assumed that only Administrators have the full authority to alter the database (create/update/delete records & users). Analysts are designed as observers who can extract intelligence by calling the dashboard summaries, while Viewers are restricted to strictly reading explicit record lines.
2. **Registration Access**: For the purposes of this assessment scenario, the `/api/auth/register` endpoint is publicly accessible so reviewers can quickly bootstrap the initial `ADMIN` test user. In a real-world scenario, the first Admin would be pre-seeded, and subsequent user creation would be locked behind an Admin-only endpoint constraint.
3. **Soft Deletion**: I assumed that financial records shouldn't be permanently purged from the database in order to retain necessary audit trails and prevent orphaned metrics. Therefore, a `deleted` boolean flag safely handles soft deletions.

## Tradeoffs Considered

1. **H2 In-Memory Database vs PostgreSQL/MySQL**: 
   - *Tradeoff*: H2 is volatile; all data is lost upon the application server restarting.
   - *Reasoning*: I elected to use H2 to guarantee a frictionless, zero-setup local testing experience for the reviewer without requiring docker-compose networks or local db installations. Spring Data JPA's architecture makes it trivial to transparently swap this out for PostgreSQL merely by modifying `application.yml` and adding the pgsql driver dependency on a production environment.
2. **Standard REST over GraphQL**:
   - *Tradeoff*: GraphQL excels at complex, relational, dynamic data fetching.
   - *Reasoning*: The requirement requested concise, well-understood operational boundaries (CRUD vs Summary). Standard REST APIs grouped cleanly by domains (`AuthController`, `RecordController`) strictly enforced separations of concern and proved the most pragmatic and accessible choice for this assignment.
3. **In-Memory Summary Aggregation**:
   - *Tradeoff*: Pulling all active records into memory to calculate dashboard totals (`DashboardService`) vs utilizing direct database JPQL `SUM()` / `GROUP BY` aggregations.
   - *Reasoning*: Given the context of a small assessment-level dataset alongside an H2 DB, utilizing intuitive Java 8 stream mapping is perfectly performant and easier to unit-test logic on. For a high-volume production environment, refactoring to specific `@Query` aggregates mapping to DTOs in the `FinancialRecordRepository` would be significantly more resource-efficient.
