-- Seed sample data for dashboard charts

-- Sample savings for admin member (id=1)
INSERT INTO savings (member_id, amount, transaction_type, transaction_date, recorded_by, notes)
VALUES 
  (1, 50000.00, 'DEPOSIT', CURRENT_TIMESTAMP - INTERVAL '5 months', 'admin', 'Initial savings deposit'),
  (1, 30000.00, 'DEPOSIT', CURRENT_TIMESTAMP - INTERVAL '4 months', 'admin', 'Monthly deposit'),
  (1, 45000.00, 'DEPOSIT', CURRENT_TIMESTAMP - INTERVAL '3 months', 'admin', 'Monthly deposit'),
  (1, 20000.00, 'DEPOSIT', CURRENT_TIMESTAMP - INTERVAL '2 months', 'admin', 'Monthly deposit'),
  (1, 60000.00, 'DEPOSIT', CURRENT_TIMESTAMP - INTERVAL '1 month', 'admin', 'Monthly deposit'),
  (1, 35000.00, 'DEPOSIT', CURRENT_TIMESTAMP, 'admin', 'Monthly deposit');

-- Sample transactions (income & expense)
INSERT INTO transactions (type, category, amount, description, transaction_date, recorded_by)
VALUES
  ('INCOME', 'Membership Fees', 150000.00, 'Q1 membership fees collected', CURRENT_TIMESTAMP - INTERVAL '3 months', 'admin'),
  ('INCOME', 'Interest', 25000.00, 'Loan interest received', CURRENT_TIMESTAMP - INTERVAL '2 months', 'admin'),
  ('INCOME', 'Fines', 15000.00, 'Meeting absence fines', CURRENT_TIMESTAMP - INTERVAL '1 month', 'admin'),
  ('INCOME', 'Membership Fees', 100000.00, 'Q2 membership fees', CURRENT_TIMESTAMP, 'admin'),
  ('EXPENSE', 'Office Supplies', 30000.00, 'Stationery and printing', CURRENT_TIMESTAMP - INTERVAL '2 months', 'admin'),
  ('EXPENSE', 'Transport', 20000.00, 'Field visit transport', CURRENT_TIMESTAMP - INTERVAL '1 month', 'admin');
