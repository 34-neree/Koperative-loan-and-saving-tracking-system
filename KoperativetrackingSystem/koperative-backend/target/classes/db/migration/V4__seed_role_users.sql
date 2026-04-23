-- Create sample members for Treasurer and Secretary
INSERT INTO members (first_name, last_name, national_id, phone, email, district, sector, cell, status, membership_date, created_at)
VALUES 
  ('Marie', 'Uwimana', '1199880012345601', '+250788111222', 'marie@koperative.rw', 'Kigali', 'Nyarugenge', 'Nyamirambo', 'ACTIVE', CURRENT_DATE, NOW()),
  ('Claude', 'Habimana', '1199880012345602', '+250788333444', 'claude@koperative.rw', 'Kigali', 'Gasabo', 'Kimironko', 'ACTIVE', CURRENT_DATE, NOW());

-- Create user accounts for Treasurer and Secretary
-- Password for both: 'password123' (BCrypt hash)
INSERT INTO users (member_id, username, password_hash, role, created_at)
VALUES 
  ((SELECT id FROM members WHERE national_id = '1199880012345601'), 'treasurer', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'TREASURER', NOW()),
  ((SELECT id FROM members WHERE national_id = '1199880012345602'), 'secretary', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SECRETARY', NOW());
