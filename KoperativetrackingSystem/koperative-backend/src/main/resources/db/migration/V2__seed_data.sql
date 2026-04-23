-- =====================================================
-- Seed Data — Default admin user and share config
-- =====================================================

-- Default member for admin
INSERT INTO members (national_id, first_name, last_name, phone, email, district, sector, cell, status)
VALUES ('1199880000000000', 'System', 'Administrator', '+250780000000', 'admin@koperative.rw', 'Kigali', 'Nyarugenge', 'Nyarugenge', 'ACTIVE');

-- Admin user (password: admin123 — BCrypt hash)
INSERT INTO users (member_id, username, password_hash, role)
VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');

-- Default share configuration
INSERT INTO share_config (share_price, minimum_shares_for_loan)
VALUES (5000.00, 5);
