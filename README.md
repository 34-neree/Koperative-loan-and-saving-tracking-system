# 🏦 Koperative Tracking System

A web application for managing a local cooperative (savings & loan group) in Rwanda. It helps cooperatives track their members, savings, loans, share capital, meetings, fines, and finances — all in one place.

---

## What Does This System Do?

This system replaces paper-based record keeping with a modern web app. Here's what each module does:

| Module | What It Does |
|--------|-------------|
| **Members** | Register and manage cooperative members (name, phone, national ID, location) |
| **Savings** | Record member deposits and withdrawals, track balances |
| **Share Capital** | Members buy shares in the cooperative, track ownership |
| **Loans** | Members apply for loans → Admin reviews → Approves/Rejects → Disburses money |
| **Repayments** | Track monthly loan repayments, flag overdue payments |
| **Meetings** | Schedule meetings and record who attended |
| **Fines** | Issue fines for missed meetings or rule violations, track payments |
| **Income & Expenses** | Track the cooperative's money coming in and going out |
| **Notifications** | Send SMS messages to members |
| **Settings** | Configure share price and loan eligibility rules |
| **Dashboard** | Overview of everything — charts, stats, and alerts |

### User Roles

| Role | What They Can Do |
|------|-----------------|
| **ADMIN** | Everything — manage members, approve loans, configure settings |
| **TREASURER** | Manage savings, shares, loans, fines, and finances |
| **SECRETARY** | Manage members and meetings |
| **MEMBER** | View own savings/shares, apply for loans |

---

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5, Spring Security (JWT), Spring Data JPA
- **Frontend**: React (Vite), Recharts for charts
- **Database**: PostgreSQL
- **Migrations**: Flyway (auto-creates tables on first run)

---


To verify they're installed, open a terminal and run:

```bash
java -version       # Should show 17 or higher
mvn -version        # Should show Maven 3.x
node -v             # Should show v18 or higher
npm -v              # Should show 9 or higher
```

---

## How to Run

### Step 1: Create the Database

Open your PostgreSQL terminal (psql) or pgAdmin and create a database:

```sql
CREATE DATABASE koperative_db;
```

> **Note**: The default database config uses:
> - Host: `localhost`
> - Port: `5432`
> - Username: `postgres`
> - Password: `auca123`
>
> If your PostgreSQL uses a different password, edit the file:
> `koperative-backend/src/main/resources/application.yml`
> and change the `password` field.

### Step 2: Start the Backend

Open a terminal in the project root folder and run:

```bash
cd koperative-backend
mvn spring-boot:run
```

Wait until you see:

```
Started KoperativeApplication in X seconds
```

This means the backend is running on **http://localhost:8080**

> On the first run, Flyway will automatically create all database tables and insert a default admin user.

### Step 3: Start the Frontend

Open a **second terminal** in the project root folder and run:

```bash
cd koperative-frontend
npm install          # Only needed the first time
npm run dev
```

You should see:

```
VITE ready in X ms
➜ Local: http://localhost:5173/
```

### Step 4: Open the App

Open your browser and go to: **http://localhost:5173**

### Step 5: Login

Use one of the default accounts:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | ADMIN — full access |
| `treasurer` | `password123` | TREASURER — finances, loans, savings |
| `secretary` | `password123` | SECRETARY — members, meetings |

Or click **"Create Account"** to register as a new MEMBER.

---

## Project Structure

```
KoperativetrackingSystem/
├── koperative-backend/          ← Java Spring Boot API
│   ├── src/main/java/rw/koperative/
│   │   ├── config/              ← Security, JWT, CORS configuration
│   │   ├── controller/          ← REST API endpoints
│   │   ├── dto/                 ← Request/Response data objects
│   │   ├── model/               ← Database entities (Member, Loan, etc.)
│   │   ├── repository/          ← Database queries
│   │   ├── service/             ← Business logic
│   │   └── exception/           ← Error handling
│   └── src/main/resources/
│       ├── application.yml      ← Database & JWT configuration
│       └── db/migration/        ← Flyway SQL scripts (auto-run)
│
├── koperative-frontend/         ← React SPA
│   ├── src/
│   │   ├── api/index.js         ← API client (Axios + JWT interceptor)
│   │   ├── components/          ← Sidebar, TopBar, Layout, ProtectedRoute
│   │   ├── context/             ← Authentication state management
│   │   ├── pages/               ← All page components
│   │   ├── main.jsx             ← App entry point & routing
│   │   └── index.css            ← Design system & styles
│   └── vite.config.js           ← Vite config with API proxy
│
└── README.md                    ← You are here
```

---

## API Endpoints

All API endpoints are prefixed with `/api/`. Here are the main ones:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login and get JWT token |
| POST | `/api/auth/register` | Register a new member account |
| GET | `/api/dashboard` | Dashboard statistics |
| GET/POST | `/api/members` | List/Create members |
| GET/POST | `/api/savings` | List/Record savings |
| GET/POST | `/api/shares` | List/Purchase shares |
| POST | `/api/loans/apply` | Apply for a loan |
| PUT | `/api/loans/{id}/approve` | Approve a loan |
| PUT | `/api/loans/{id}/disburse` | Disburse a loan |
| GET/POST | `/api/meetings` | List/Create meetings |
| GET/POST | `/api/fines` | List/Issue fines |
| GET/POST | `/api/transactions` | List/Record income/expenses |
| POST | `/api/notifications/send` | Send a notification |

---



## License

This project was built for educational purposes at AUCA (Adventist University of Central Africa), Rwanda.
