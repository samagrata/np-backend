ALTER TABLE receivables
DROP INDEX fund_id;

ALTER TABLE expenses
DROP INDEX fund_id;

CREATE INDEX idx_receivables_fund_id ON receivables (fund_id);

CREATE INDEX idx_expenses_fund_id ON expenses (fund_id);
