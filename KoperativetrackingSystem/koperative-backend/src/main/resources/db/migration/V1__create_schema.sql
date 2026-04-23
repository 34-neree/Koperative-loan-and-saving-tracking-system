-- =====================================================
-- Koperative Tracking System — Database Schema
-- PostgreSQL 15+
-- =====================================================

-- Members
CREATE TABLE members (
    id              BIGSERIAL PRIMARY KEY,
    national_id     VARCHAR(20) NOT NULL UNIQUE,
    first_name      VARCHAR(50) NOT NULL,
    last_name       VARCHAR(50) NOT NULL,
    phone           VARCHAR(20) NOT NULL,
    email           VARCHAR(100),
    cell            VARCHAR(50),
    sector          VARCHAR(50),
    district        VARCHAR(50),
    next_of_kin     VARCHAR(100),
    next_of_kin_phone VARCHAR(20),
    membership_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Users (Auth)
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    member_id       BIGINT REFERENCES members(id) ON DELETE SET NULL,
    username        VARCHAR(50) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Savings
CREATE TABLE savings (
    id                  BIGSERIAL PRIMARY KEY,
    member_id           BIGINT NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    amount              DECIMAL(15,2) NOT NULL,
    transaction_type    VARCHAR(20) NOT NULL,
    transaction_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    recorded_by         VARCHAR(50),
    notes               TEXT
);

-- Share Config
CREATE TABLE share_config (
    id                      BIGSERIAL PRIMARY KEY,
    share_price             DECIMAL(15,2) NOT NULL DEFAULT 5000.00,
    minimum_shares_for_loan INT NOT NULL DEFAULT 5,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Shares
CREATE TABLE shares (
    id                          BIGSERIAL PRIMARY KEY,
    member_id                   BIGINT NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    number_of_shares            INT NOT NULL,
    share_price_at_purchase     DECIMAL(15,2) NOT NULL,
    purchase_date               DATE NOT NULL DEFAULT CURRENT_DATE,
    total_value                 DECIMAL(15,2) NOT NULL,
    status                      VARCHAR(20) NOT NULL DEFAULT 'PAID'
);

-- Loans
CREATE TABLE loans (
    id                  BIGSERIAL PRIMARY KEY,
    member_id           BIGINT NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    amount_requested    DECIMAL(15,2) NOT NULL,
    amount_approved     DECIMAL(15,2),
    interest_rate       DECIMAL(5,2) NOT NULL DEFAULT 10.00,
    term_months         INT NOT NULL DEFAULT 12,
    purpose             TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'APPLIED',
    applied_date        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_date       TIMESTAMP,
    disbursed_date      TIMESTAMP,
    guarantor1_id       BIGINT REFERENCES members(id),
    guarantor2_id       BIGINT REFERENCES members(id)
);

-- Loan Repayments
CREATE TABLE loan_repayments (
    id                  BIGSERIAL PRIMARY KEY,
    loan_id             BIGINT NOT NULL REFERENCES loans(id) ON DELETE CASCADE,
    due_date            DATE NOT NULL,
    amount_due          DECIMAL(15,2) NOT NULL,
    amount_paid         DECIMAL(15,2) NOT NULL DEFAULT 0,
    paid_date           TIMESTAMP,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    penalty_applied     DECIMAL(15,2) NOT NULL DEFAULT 0
);

-- Meetings
CREATE TABLE meetings (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    meeting_date    TIMESTAMP NOT NULL,
    location        VARCHAR(200),
    agenda          TEXT,
    minutes         TEXT,
    created_by      VARCHAR(50),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Meeting Attendance
CREATE TABLE meeting_attendance (
    id              BIGSERIAL PRIMARY KEY,
    meeting_id      BIGINT NOT NULL REFERENCES meetings(id) ON DELETE CASCADE,
    member_id       BIGINT NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    attended        BOOLEAN NOT NULL DEFAULT FALSE,
    fine_issued     BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(meeting_id, member_id)
);

-- Fines
CREATE TABLE fines (
    id              BIGSERIAL PRIMARY KEY,
    member_id       BIGINT NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    reason          VARCHAR(255) NOT NULL,
    amount          DECIMAL(15,2) NOT NULL,
    issued_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
    paid_date       TIMESTAMP,
    reference_id    BIGINT,
    reference_type  VARCHAR(50)
);

-- Transactions (Income & Expenses)
CREATE TABLE transactions (
    id                  BIGSERIAL PRIMARY KEY,
    type                VARCHAR(20) NOT NULL,
    category            VARCHAR(100) NOT NULL,
    amount              DECIMAL(15,2) NOT NULL,
    description         TEXT,
    transaction_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    recorded_by         VARCHAR(50),
    receipt_number      VARCHAR(50)
);

-- Notifications / SMS Log
CREATE TABLE notifications (
    id              BIGSERIAL PRIMARY KEY,
    member_id       BIGINT REFERENCES members(id) ON DELETE SET NULL,
    phone           VARCHAR(20) NOT NULL,
    message         TEXT NOT NULL,
    channel         VARCHAR(20) NOT NULL DEFAULT 'SYSTEM',
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    sent_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_members_national_id ON members(national_id);
CREATE INDEX idx_members_status ON members(status);
CREATE INDEX idx_savings_member_id ON savings(member_id);
CREATE INDEX idx_shares_member_id ON shares(member_id);
CREATE INDEX idx_loans_member_id ON loans(member_id);
CREATE INDEX idx_loans_status ON loans(status);
CREATE INDEX idx_loan_repayments_loan_id ON loan_repayments(loan_id);
CREATE INDEX idx_loan_repayments_status ON loan_repayments(status);
CREATE INDEX idx_fines_member_id ON fines(member_id);
CREATE INDEX idx_fines_status ON fines(status);
CREATE INDEX idx_transactions_type ON transactions(type);
CREATE INDEX idx_notifications_member_id ON notifications(member_id);
