# Finance Dashboard API Documentation

### Base URL for Deployed Environment: 
`https://finance-backend-springboot.onrender.com`

---

## 🏗️ 1. Authentication (`/api/auth`)
*All users must register and log in to receive a JWT Token. This token must be passed as `Authorization: Bearer <TOKEN>` in the headers of all subsequent requests.*

### A. Register User
* **Method:** `POST`
* **URL:** `/api/auth/register`
* **Security:** Public (For setup purposes; anyone can register)
* **JSON Body:**
```json
{
  "username": "adminUser",
  "password": "securePassword",
  "role": "ADMIN" 
}
```
*(Acceptable roles: `ADMIN`, `ANALYST`, `VIEWER`)*

### B. Login
* **Method:** `POST`
* **URL:** `/api/auth/login`
* **Security:** Public
* **JSON Body:**
```json
{
  "username": "adminUser",
  "password": "securePassword"
}
```
* **Response:** Returns your `token` string used for Auth.

---

## 👥 2. User Management (`/api/users`)
*This entire domain is protected and strictly requires the **ADMIN** role.*

### A. Get All Users
* **Method:** `GET`
* **URL:** `/api/users`

### B. Get User By ID
* **Method:** `GET`
* **URL:** `/api/users/{id}`

### C. Update User Role
* **Method:** `PUT`
* **URL:** `/api/users/{id}/role?role=ANALYST`
* **Query Params:** You must provide `role` in the URL schema.

### D. Toggle User Active Status
* **Method:** `PUT`
* **URL:** `/api/users/{id}/status?active=false`
* **Query Params:** Provide `active=true` or `active=false`.

---

## 💰 3. Financial Records (`/api/records`)
*Basic reading is accessible to everyone. Editing strictly requires the **ADMIN** role.*

### A. Create Record  *(Requires Admin)*
* **Method:** `POST`
* **URL:** `/api/records`
* **JSON Body:**
```json
{
  "amount": 2500.50,
  "type": "INCOME",
  "category": "Salary",
  "date": "2026-04-01",
  "notes": "Monthly Paycheck"
}
```
*(Acceptable types: `INCOME`, `EXPENSE`)*

### B. Read & Filter Records *(Requires Admin, Analyst, or Viewer)*
* **Method:** `GET`
* **URL:** `/api/records`
* **Query Params (All Optional):**
  - `type` (e.g., `INCOME`)
  - `category` (e.g., `Salary`)
  - `startDate` (e.g., `2026-04-01` - Format: YYYY-MM-DD)
  - `endDate` (e.g., `2026-04-30` - Format: YYYY-MM-DD)
  - `page` / `size` (for Pagination limits)

### C. Get Specific Record By ID *(All Roles)*
* **Method:** `GET`
* **URL:** `/api/records/{id}`

### D. Update Existing Record *(Requires Admin)*
* **Method:** `PUT`
* **URL:** `/api/records/{id}`
* **JSON Body:** Same rules as Create endpoint.

### E. Soft-Delete Record *(Requires Admin)*
* **Method:** `DELETE`
* **URL:** `/api/records/{id}`

---

## 📊 4. Dashboard Summary (`/api/dashboard`)
*This route handles complex aggregations so it strips out soft-deleted files.*

### A. Get Summary Metrics *(Requires Admin or Analyst)*
* **Method:** `GET`
* **URL:** `/api/dashboard/summary`
* **Response Details:** Calculates Total Incomes, Total Expenses, your Net Account Balance, groups all amounts categorically, and provides an array of your 5 most recently created rows.
